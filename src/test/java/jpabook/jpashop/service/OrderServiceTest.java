package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.item.Book;
import jpabook.jpashop.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember("mem1", new Address("seuol", "river", "123-123"));

        Book book = createBook("시골 JPA", 10000, 10);

        //when
        int ordercount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), ordercount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER,getOrder.getStatus());
        assertEquals("주문한 상품 종류수가 정확 해야 한다.",1,  getOrder.getOrderItems().size());
        assertEquals("총 가격은 가격 * 수량이다.", getOrder.getTotalPrice(), 10000 * ordercount);
        assertEquals("주문수량만큼 재고가 줄어야 한다.",10 - ordercount, book.getStockQuantity());
    }


    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember("mem1", new Address("seuol", "river", "123-123"));
        Item item = createBook("시골 JPA", 10000, 10);

        int ordercount = 11;

        //when
        Long orderId = orderService.order(member.getId(), item.getId(), ordercount);


        //then
        fail("재고 수량 예외가 발생해야 한다.");
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember("mem1", new Address("seuol", "river", "123-123"));
        Item book = createBook("시골 JPA", 10000, 10);

        int ordercount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), ordercount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("취소시 상태는 CANCLE이다.", getOrder.getStatus(),OrderStatus.CANCEL);
        assertEquals("주문이 취소되면 재고가 원복되야 한다..", 10, book.getStockQuantity());

    }
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name, Address address) {
        Member member = new Member();

        member.setName(name);
        member.setAddress(address);
        em.persist(member);
        return member;
    }

}