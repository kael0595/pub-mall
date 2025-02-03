package com.example.demo.order.controller;

import com.example.demo.cashLog.service.CashLogService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.oauth.dto.PrincipalDetails;
import com.example.demo.order.entity.Order;
import com.example.demo.order.service.OrderService;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final ProductService productService;

    private final MemberService memberService;

    private final CashLogService cashLogService;

    @Value("${custom.paymentSecretKey}")
    private String paymentSecretKey;

    @PostMapping("/create")
    public String createOrder(@AuthenticationPrincipal PrincipalDetails principalDetails,
                              @RequestParam("productId") Long productId,
                              @RequestParam("amount") int amount) {

        Member member = memberService.findByUsername(principalDetails.getUsername());

        Order order = orderService.createOrder(member, productId, amount);

        Long orderId = order.getId();

        return "redirect:/order/detail/" + orderId;
    }

    @GetMapping("/detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Long orderId, Model model) {

        Order order = orderService.findById(orderId);
        Member member = memberService.findByUsername(order.getMember().getUsername());
        Product product = productService.findByOrderId(order.getId());
        model.addAttribute("order", order);
        model.addAttribute("member", member);
        model.addAttribute("product", product);
        return "order/detail";
    }

    @GetMapping("/{id}/success")
    public String paymentResult(Model model,
                                @PathVariable(value = "id") Long id,
                                @RequestParam(value = "productId") Long productId,
                                @RequestParam(value = "orderId") String orderId,
                                @RequestParam(value = "amount") Integer amount,
                                @RequestParam(value = "quantity") Integer quantity,
                                @RequestParam(value = "paymentKey") String paymentKey,
                                Principal principal) throws Exception {

        String secretKey = paymentSecretKey;

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        JSONObject obj = new JSONObject();

        obj.put("orderId", orderId);
        obj.put("amount", amount);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;
        model.addAttribute("isSuccess", isSuccess);

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();
        model.addAttribute("responseStr", jsonObject.toJSONString());
        System.out.println(jsonObject.toJSONString());

        model.addAttribute("method", (String) jsonObject.get("method"));
        model.addAttribute("orderName", (String) jsonObject.get("orderName"));

        if (((String) jsonObject.get("method")) != null) {
            if (((String) jsonObject.get("method")).equals("카드")) {
                model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
            } else if (((String) jsonObject.get("method")).equals("가상계좌")) {
                model.addAttribute("accountNumber", (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
            } else if (((String) jsonObject.get("method")).equals("계좌이체")) {
                model.addAttribute("bank", (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
            } else if (((String) jsonObject.get("method")).equals("휴대폰")) {
                model.addAttribute("customerMobilePhone", (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
            }
        } else {
            model.addAttribute("code", (String) jsonObject.get("code"));
            model.addAttribute("message", (String) jsonObject.get("message"));
        }

        amount = quantity;

        Member member = memberService.findByUsername(principal.getName());
        Product product = productService.findById(productId);
        Order order = orderService.createOrder(member, productId, amount);

        log.info("amount : {}", amount);

        model.addAttribute("member", member);
        model.addAttribute("product", product);
        model.addAttribute("order", order);
        cashLogService.addCashLog(member, product, amount);
        return "order/success";
    }

    @GetMapping("/fail")
    public String paymentResult(Model model,
                                @RequestParam("message") String message,
                                @RequestParam("code") int code) throws Exception {

        model.addAttribute("message", message);
        model.addAttribute("code", code);

        return "order/fail";
    }

}
