package ssg.osd.osapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssg.osd.osapi.dto.*;
import ssg.osd.osapi.repository.ProductRepository;
import ssg.osd.osapi.service.OrderService;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
public class OrderUiController {

    private final OrderService orderService;

    private final ProductRepository productRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("lookupForm", new LookupForm());
        model.addAttribute("cancelForm", new CancelForm());
        model.addAttribute("products", productRepository.findAll());
        return "order-ui";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute OrderForm orderForm, RedirectAttributes redirectAttributes) {
        try {
            OrderResponseDto result = orderService.createOrder(orderForm.toDto());

            redirectAttributes.addFlashAttribute("result", "주문 완료! 주문 ID: " + result.getOrderId() + ", 주문 금액 : " + result.getTotalAmount());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("result", "주문 실패: " + e.getMessage());

            e.printStackTrace();
        }

        return "redirect:/ui";
    }

    @PostMapping("/lookup")
    public String lookup (@ModelAttribute LookupForm form, RedirectAttributes redirectAttributes) {
        try {
            OrderDetailResponseDto order = orderService.getOrderDetail(form.getOrderId());

            redirectAttributes.addFlashAttribute("result", "조회 완료! 주문 ID: " + order.getOrderId() + ", 총 금액: " + order.getTotalAmount());

            redirectAttributes.addFlashAttribute("items", order.getItems()); // 주문 항목 리스트
            redirectAttributes.addFlashAttribute("totalAmount", order.getTotalAmount()); // 주문 총 금액
            redirectAttributes.addFlashAttribute("orderId", order.getOrderId()); // 주문 ID
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("result", "조회 실패: " + e.getMessage());

            e.printStackTrace();
        }

        return "redirect:/ui";
    }

    @PostMapping("/cancel")
    public String cancel(@ModelAttribute CancelForm form, RedirectAttributes redirectAttributes) {
        try {
            CancelOrderItemResponseDto result = orderService.cancelOrderItem(form.getOrderId(), form.getProductId());
            redirectAttributes.addFlashAttribute("result", "취소 완료! 환불: " + result.getRefundAmount());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("result", "취소 실패: " + e.getMessage());

            e.printStackTrace();
        }
        return "redirect:/ui";
    }
}
