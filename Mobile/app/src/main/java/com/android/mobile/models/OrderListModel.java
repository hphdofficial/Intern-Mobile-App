package com.android.mobile.models;

import java.util.List;

public class OrderListModel {
    private int id;
    private int member_id;
    private String txn_ref;
    private double amount;
    private String order_info;
    private String response_code;
    private String transaction_no;
    private String bank_code;
    private String pay_date;
    private String status;
    private String created_at;
    private String updated_at;
    private String ten;
    private String giao_hang;
    private List<DetailCart> detail_carts;

    public static class DetailCart {
        private int id;
        private int id_order;
        private int id_product;
        private int quantity;
        private Product product;

        public static class Product {
            private int ProductID;
            private String ProductName;
            private int SupplierID;
            private double UnitPrice;
            private int UnitsInStock;
            private String CategoryName;
            private String link_image;
            private String SupplierName;

            public int getProductID() {
                return ProductID;
            }

            public void setProductID(int productID) {
                ProductID = productID;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String productName) {
                ProductName = productName;
            }

            public int getSupplierID() {
                return SupplierID;
            }

            public void setSupplierID(int supplierID) {
                SupplierID = supplierID;
            }

            public double getUnitPrice() {
                return UnitPrice;
            }

            public void setUnitPrice(double unitPrice) {
                UnitPrice = unitPrice;
            }

            public int getUnitsInStock() {
                return UnitsInStock;
            }

            public void setUnitsInStock(int unitsInStock) {
                UnitsInStock = unitsInStock;
            }

            public String getCategoryName() {
                return CategoryName;
            }

            public void setCategoryName(String categoryName) {
                CategoryName = categoryName;
            }

            public String getLink_image() {
                return link_image;
            }

            public void setLink_image(String link_image) {
                this.link_image = link_image;
            }

            public String getSupplierName() {
                return SupplierName;
            }

            public void setSupplierName(String supplierName) {
                SupplierName = supplierName;
            }
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId_order() {
            return id_order;
        }

        public void setId_order(int id_order) {
            this.id_order = id_order;
        }

        public int getId_product() {
            return id_product;
        }

        public void setId_product(int id_product) {
            this.id_product = id_product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getTxn_ref() {
        return txn_ref;
    }

    public void setTxn_ref(String txn_ref) {
        this.txn_ref = txn_ref;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOrder_info() {
        return order_info;
    }

    public void setOrder_info(String order_info) {
        this.order_info = order_info;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getGiao_hang() {
        return giao_hang;
    }

    public void setGiao_hang(String giao_hang) {
        this.giao_hang = giao_hang;
    }

    public List<DetailCart> getDetail_carts() {
        return detail_carts;
    }

    public void setDetail_carts(List<DetailCart> detail_carts) {
        this.detail_carts = detail_carts;
    }
}