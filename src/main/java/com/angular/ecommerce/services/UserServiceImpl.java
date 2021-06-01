package com.angular.ecommerce.services;

import com.angular.ecommerce.config.EmailCfg;
import com.angular.ecommerce.dto.CartDTO;
import com.angular.ecommerce.dto.CartItemDTO;
import com.angular.ecommerce.dto.RegisterDTO;
import com.angular.ecommerce.entities.*;
import com.angular.ecommerce.repositories.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Transactional
@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ICartItemRepository cartItemRepository;
    @Autowired
    private ICartRepository cartRepository;
    @Autowired
    private IOrderItemRepository orderItemRepository;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailCfg emailCfg;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public User addUser(RegisterDTO registerDTO) {

        User user = modelMapper.map(registerDTO,User.class);
        Cart cart = new Cart();
        Cart cart1 = cartRepository.save(cart);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().clear();
        for (String role : registerDTO.getRole()){
            Role role1 = roleRepository.findByRole(role);
            user.getRoles().add(role1);
        }
        User u =  userRepository.save(user);
        cart1.setUser(user);
        user.setCart(cart1);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(this.emailCfg.getUsername());
        mailMessage.setTo(u.getEmail());
        mailMessage.setSubject("welcome");
        mailMessage.setText("your UserName is : "+u.getUsername()+" you password is : "+registerDTO.getPassword());
        javaMailSender.send(mailMessage);
        return u;
    }

    @Override
    public Boolean addToCart(Long id, CartItemDTO cartItemDTO) {
        Optional<User> user = userRepository.findById(id);
        AtomicReference<Boolean> added = new AtomicReference<>(false);
        CartItem cartItem = modelMapper.map(cartItemDTO,CartItem.class);
        Optional<Product> product = productRepository.findById(cartItem.getProduct().getId());
 if (user.isPresent() && product.isPresent()) {
            if (user.get().getCart().getCartItems().stream().anyMatch(cartItem1 -> cartItem1.getProduct().getId().equals(cartItem.getProduct().getId()))) {
                user.get().getCart().getCartItems().stream().forEach(cartItem1 -> {
                   if (cartItem1.getProduct().getId().equals(cartItem.getProduct().getId())){
                       if (product.get().getStock()>=(cartItem1.getQuantity()+cartItem.getQuantity())){
                           cartItem1.setQuantity(cartItem1.getQuantity()+cartItem.getQuantity());
                           cartRepository.save(user.get().getCart());
                           added.set(true);
                       }
                   }
                 });
         }else{
                if (cartItem.getQuantity()<=product.get().getStock()){
                    user.get().getCart().getCartItems().add(cartItem);
                    cartItem.setCart(user.get().getCart());
                    added.set(true);
                }
            }


        }

        return added.get();
    }

    @Override
    public Boolean emptyCart(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
           user.get().getCart().getCartItems().clear();
           cartRepository.save(user.get().getCart());
           return true;
        }

        return false;
    }

    @Override
    public Boolean deleteCartItem(Long cartItemId) {

        Optional<CartItem> cartItem=cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()){
            cartItemRepository.delete(cartItem.get());
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateCartItemQuantity(Long cartItemId, int quantity) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if(cartItem.isPresent()){
            if (cartItem.get().getProduct().getStock()>=quantity && quantity>0){
                cartItem.get().setQuantity(quantity);
                cartItemRepository.save(cartItem.get());

            }else if (quantity==0){
                cartItemRepository.delete(cartItem.get());
            }
        }
        return true;
    }

    @Override
    public CartDTO getCart(Long id) {
        Optional<User> user = userRepository.findById(id);
        CartDTO cartDTO = null;
        if (user.isPresent()){
            cartDTO = modelMapper.map(user.get().getCart(),CartDTO.class);
        }
        return cartDTO;
    }

    @Override
    public Boolean makeOrder(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Order o = new Order();
        Boolean orderMade=false;
        if (user.isPresent()){
            Order order = orderRepository.save(o);
            order.setUser(user.get());
            order.setShippingAddress(user.get().getAddress());
            user.get().getCart().getCartItems().stream().forEach(cartItem ->{
                Optional<Product> product = productRepository.findById(cartItem.getProduct().getId());
                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setProduct(cartItem.getProduct());
                product.get().setStock(product.get().getStock()-cartItem.getQuantity());
                order.getOrderItems().add(orderItem);
                orderItem.setOrder(order);
            });
            orderMade=true;
           Order order1 = orderRepository.save(order);
           if(order1 != null){
               MimeMessage message = javaMailSender.createMimeMessage();

               MimeMessageHelper helper = null;
               try {
                   helper = new MimeMessageHelper(message, true);
                   helper.setFrom(this.emailCfg.getUsername());
                   helper.setTo(user.get().getEmail());
                   helper.setSubject("welcome");
                   helper.setText(" UserName is : "+user.get().getUsername()+" this is your Order : ");
                   helper.addAttachment("Order.xlsx", GenerateOrderExcel(order1));
                   javaMailSender.send(message);
               } catch (MessagingException e) {
                   e.printStackTrace();
               } catch (InvalidFormatException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }

            user.get().getCart().getCartItems().clear();
        }
        return orderMade;
    }


    public static File GenerateOrderExcel(Order order) throws IOException, InvalidFormatException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Product Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Price");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Quantity");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Total Order Item Price");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Total Order Price");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        for (int i=2 ;i<order.getOrderItems().size()+2;i++){


            Row row = sheet.createRow(i);

            Cell cell = row.createCell(0);
            cell.setCellValue(order.getOrderItems().get(i-2).getProduct().getName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(order.getOrderItems().get(i-2).getProduct().getPrice());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(order.getOrderItems().get(i-2).getQuantity());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(order.getOrderItems().get(i-2).getProduct().getPrice() * order.getOrderItems().get(i-2).getQuantity());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(order.getOrderItems().get(i-2).getProduct().getPrice());
            cell.setCellStyle(style);


        }
        Row row1 = sheet.createRow(order.getOrderItems().size()+2);
        Cell cell = row1.createCell(4);
        cell.setCellValue(order.getTotalPrice());
        cell.setCellStyle(style);
        File f = new File("poi-generated-file.xlsx");
        FileOutputStream fileOut = new FileOutputStream(f);
        workbook.write(fileOut);
        fileOut.close();
        // Closing the workbook
        workbook.close();
        return f;
    }
}
