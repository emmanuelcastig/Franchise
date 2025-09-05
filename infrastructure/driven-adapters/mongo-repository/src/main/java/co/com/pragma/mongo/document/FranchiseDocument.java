package co.com.pragma.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("franquicias")
public class FranchiseDocument {
    @Id
    private String id;
    private String name;
    private List<BranchDocument> branches;

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class BranchDocument {
        private String name;
        private List<ProductDocument> products;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class ProductDocument {
        private String name;
        private int stock;
    }
}