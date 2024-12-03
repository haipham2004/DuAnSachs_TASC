package com.example.books_service.service.imp;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.exception.NotfoundException;
import com.example.books_service.repository.BooksServiceRepository;
import com.example.books_service.service.BooksService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BooksServiceImp implements BooksService {

    private static final String BOOK_CACHE_KEY = "all_books";

    private final RedisTemplate<String, Object> redisTemplate;

    private BooksServiceRepository booksServiceRepository;

    @Autowired
    public BooksServiceImp(RedisTemplate<String, Object> redisTemplate, BooksServiceRepository booksServiceRepository) {
        this.redisTemplate = redisTemplate;
        this.booksServiceRepository = booksServiceRepository;
    }

    @Override
    public PageResponse<BooksResponse> findAllBooksPage(int pageSize, int offset) {
        long startTime = System.currentTimeMillis(); // Bắt đầu đo thời gian

        // Truy vấn từ database (hoặc phương thức khác)
        PageResponse<BooksResponse> response = booksServiceRepository.findBooksPage(pageSize, offset);

        long elapsedTime = System.currentTimeMillis() - startTime; // Tính thời gian đã trôi qua

        // Log thời gian truy xuất dữ liệu
        log.info("Thời gian truy vấn findAllBooksPage với pageSize: {} và offset: {} là: {} ms", pageSize, offset, elapsedTime);

        return response;
    }

//    @Override
//    public PageResponse<BooksResponse> findAllBooksPage(int pageSize, int offset) {
//        String cacheKey = BOOK_CACHE_KEY + "_pageSize_" + pageSize + "_offset_" + offset;
//
//        // Kiểm tra cache Redis
//        long startTime = System.currentTimeMillis();  // Thời gian bắt đầu đo
//        String cachedResponseJson = (String) redisTemplate.opsForValue().get(cacheKey);
//
//        if (cachedResponseJson != null) {
//            // Log khi lấy từ Redis cache
//            long elapsedTime = System.currentTimeMillis() - startTime;  // Thời gian thực hiện
//            log.info("Truy vấn dữ liệu từ Redis cache với key: {}. Thời gian truy vấn: {} ms", cacheKey, elapsedTime);
//
//            try {
//                ObjectMapper objectMapper = new ObjectMapper();
//                return objectMapper.readValue(cachedResponseJson, PageResponse.class);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Nếu không có trong cache, truy vấn từ database
//        startTime = System.currentTimeMillis();  // Đo lại thời gian khi truy vấn database
//        PageResponse<BooksResponse> response = booksServiceRepository.findBooksPage(pageSize, offset);
//
//        long elapsedTime = System.currentTimeMillis() - startTime;  // Thời gian thực hiện
//        log.info("Truy vấn dữ liệu từ database. Thời gian truy vấn: {} ms", elapsedTime);
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String responseJson = objectMapper.writeValueAsString(response);
//            redisTemplate.opsForValue().set(cacheKey, responseJson, 30, TimeUnit.MINUTES);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }



    @Override
    public BooksResponse findById(Integer id) {
        return booksServiceRepository.findById(id);
    }

    @Override
//    @CacheEvict(value = "books", key = "'findAllBooksPage'")
    public BooksRequest save(BooksRequest booksRequest) {
        return booksServiceRepository.save(booksRequest);
    }


    @Override
    public BooksRequest update(BooksRequest booksRequest, Integer id) {
        return booksServiceRepository.update(booksRequest, id);
    }


    @Override
    public void deleteById(boolean delete, Integer id) {
        booksServiceRepository.deleteById(delete,id);
    }


    @Override
    public PageResponse<BooksResponse> findBooksPage3(String nameBook, String nameAuthor, String namePublisher,
                                                      String nameCategory, double priceMin, double priceMax,
                                                      int pageNumber, int pageSize, String sort) {
        return booksServiceRepository.findBooksPage3( nameBook,  nameAuthor,  namePublisher,  nameCategory, priceMin, priceMax,  pageNumber,  pageSize, sort );
    }

    @Override
    public List<BooksResponse> getAvailableQuantity(List<Integer> bookIds) {
        // Lấy thông tin tất cả sách có trong danh sách bookIds từ repository
        List<BooksResponse> availableBooks = booksServiceRepository.getAvailableQuantity(bookIds);

        // Kiểm tra nếu sách không tồn tại
        List<Integer> foundBookIds = availableBooks.stream()
                .map(BooksResponse::getBookId)
                .collect(Collectors.toList());

        // Danh sách các bookIds không tồn tại trong cơ sở dữ liệu
        List<Integer> missingBookIds = bookIds.stream()
                .filter(bookId -> !foundBookIds.contains(bookId))
                .collect(Collectors.toList());

        // Nếu có bất kỳ sách nào không tồn tại, ném ngoại lệ NotFoundException cho những sách đó
        if (!missingBookIds.isEmpty()) {
            String missingBooks = missingBookIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            throw new NotfoundException("Không tìm thấy sách với bookIds: " + missingBooks);
        }

        // Kiểm tra nếu sách có số lượng bằng 0 (hết hàng)
        List<String> outOfStockBooks = availableBooks.stream()
                .filter(book -> book.getQuantity() == 0)
                .map(book -> book.getBookId() + " - " + book.getTitle()) // Kèm theo tên sách
                .collect(Collectors.toList());

        // Nếu có sách hết hàng, ném ngoại lệ NotFoundException với thông báo hết hàng
        if (!outOfStockBooks.isEmpty()) {
            String outOfStockMessage = outOfStockBooks.stream()
                    .collect(Collectors.joining(", "));
            throw new NotfoundException("Sách hết hàng với bookIds và title: " + outOfStockMessage);
        }

        // Trả về danh sách các sách có sẵn
        return availableBooks;
    }


    @Override
    public void decreaseQuantity(Integer bookId, Integer quantity) {
        booksServiceRepository.decreaseQuantity(bookId,quantity);
    }

    @Override
    public void increaseQuantity(Integer bookId, Integer quantity) {
        booksServiceRepository.increaseQuantity(bookId,quantity);
    }

    @Override
    public List<BooksResponse> reduceQuantitys(Integer bookId, Integer quantity) {
        return booksServiceRepository.reduceQuantitys(bookId,quantity);
    }

    @Override
    public List<BooksResponse> increaseQuantitys(Integer bookId, Integer quantity) {
        return booksServiceRepository.increaseQuantitys(bookId,quantity);
    }

    @Override
    public BooksRequest reserve(BooksRequest desiredBook, Integer orderId) throws Exception {
        return booksServiceRepository.reserve(desiredBook,orderId);
    }

    @Override
    public void cancelReservation(BooksRequest bookToCancel, Integer orderId) {
        booksServiceRepository.cancelReservation(bookToCancel,orderId);
    }




}
