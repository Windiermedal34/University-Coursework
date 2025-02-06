/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package adam.coursework.ctrl;

import adam.coursework.bus.StartService;
import adam.coursework.ents.Address;
import adam.coursework.ents.Administrator;
import adam.coursework.ents.Basket;
import adam.coursework.ents.Books;
import adam.coursework.ents.Orders;
import adam.coursework.ents.PaymentMethod;
import adam.coursework.ents.Person;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 * The controller for the artefact
 * @author adamt
 */
@Named(value = "start")
@SessionScoped
public class StartCtrl implements Serializable {

    /**
     * Creates a new instance of start
     */
    public StartCtrl() {
    }

    /**
     * The forename entered when registering a new account
     */
    private String name;
    /**
     * The surname entered when registering a new account
     */
    private String surname;
    /**
     * The email address entered when registering a new account
     */
    private String emailAddress;
    /**
     * The password entered when registering a new account
     */
    private String password;
    /**
     * Whether the account being created is a customer or an administrator
     */
    private String role;

    /**
     * The forename associated with the login email address and password
     */
    private String loginName;
    /**
     * The surname associated with the login email address and password
     */
    private String loginSurname;
    /**
     * The email address being used to login to an account
     */
    private String loginEmail;
    /**
     * The password being used to login to an account
     */
    private String loginPassword;

    /**
     * The title of a book
     */
    private String title;
    /**
     * The author of a book
     */
    private String author;
    /**
     * The edition of a book
     */
    private String edition;
    /**
     * The price of a book
     */
    private String price;
    /**
     * The number of copies of a book
     */
    private String copy;
    
    /**
     * The publisher of the book
     */
    private String publisher;
    
    /**
     * The year the book was published
     */
    private String year;

    /**
     * The year a book was published used as a filter parameter
     */
    private String yearOfPublication;
    /**
     * A range of prices for a book used as a filter parameter
     */
    private String priceRange;
    /**
     * The status of an order used by the system administrator to update the status of orders
     */
    private String newStatus;
    /**
     * A Boolean value used to render objects in the view for logged out users
     */
    private boolean loggedOutBrowser = true;

    public boolean isLoggedOutBrowser() {
        return loggedOutBrowser;
    }

    public void setLoggedOutBrowser(boolean loggedOutBrowser) {
        this.loggedOutBrowser = loggedOutBrowser;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
    /**
     * A list of every book currently in the database
     */
    private List<Books> booksInSystem;

    /**
     * Getter calls the business logic which retrieves a list of every book in the database and assigns it to the booksInSystem list
     * @return the list of books currently stored in the database
     */
    public List<Books> getBooksInSystem() {
        booksInSystem = ss.retrieveBooks();
        return booksInSystem;
    }

    public void setBooksInSystem(List<Books> booksInSystem) {
        this.booksInSystem = booksInSystem;
    }

    /**
     * An object of the Books entity
     */
    private Books book = new Books();

    public Books getBook() {
        return book;
    }

    public void setBook(Books book) {
        this.book = book;
    }
    
    /**
     * An object of the Basket entity
     */
    private Basket sb = new Basket();

    public Basket getSb() {
        return sb;
    }

    public void setSb(Basket sb) {
        this.sb = sb;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginSurname() {
        return loginSurname;
    }

    public void setLoginSurname(String loginSurname) {
        this.loginSurname = loginSurname;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCopy() {
        return copy;
    }

    public void setCopy(String copy) {
        this.copy = copy;
    }

    public String getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(String yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String Author) {
        this.author = Author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * The object of the StartService class, the business logic component of the artefact
     */
    @EJB
    private StartService ss;
    
    /**
     * This function calls the business logic to register customers in the database and passes the account details with it
     */
    public void registerCustomer() {
        ss.startStarting(name, surname, emailAddress, longTerm);
    }

    /**
     * This function calls the business logic to register administrators in the database and passes account details with it
     */
    public void registerAdmin() {
        ss.registerAdmin(emailAddress, longTerm);
    }
    
    /**
     * This function calls the business logic to add books to the database
     */
    public void addBook(){
        book.setTitle(title);
        book.setAuthor(author);
        book.setCopies(Integer.parseInt(copy));
        book.setEdition(edition);
        book.setPrice(price);
        book.setPublisher(publisher);
        book.setYearOfPublication(year);
        ss.recordBook(book);
    }
    
    /**
     * A Boolean variable that determines if the data table of all books in the database is rendered
     */
    private boolean browseAllFlag = true;

    public boolean isBrowseAllFlag() {
        return browseAllFlag;
    }

    public void setBrowseAllFlag(boolean browseAllFlag) {
        this.browseAllFlag = browseAllFlag;
    }

    /**
     * A Boolean variable that determines if the data table of books searched for in the database is rendered
     */
    private boolean browseFlag = false;

    public boolean isBrowseFlag() {
        return browseFlag;
    }

    public void setBrowseFlag(boolean browseFlag) {
        this.browseFlag = browseFlag;
    }

    /**
     * Renders the data table showing books searched for and hides the data table showing every book in the database
     */
    public void showBookBrowser() {
        browseFlag = true;
        browseAllFlag = false;
    }
    
    /**
     * hides the data table showing books searched for and renders the data table showing every book in the database
     */
    public void resetBrowse(){
        browseFlag = false;
        browseAllFlag = true;
    }

    /**
     * A list of the Books entity used to populate the data table that prints every book stored in the database
     */
    private List<Books> allBooks;

    /**
     * retrieves the list of every book in the database from the business logic, assigns this to the allBooks list
     * @return returns the list of every book in the database
     */
    public List<Books> getAllBooks() {
        allBooks = ss.retrieveBooks();
        return allBooks;
    }

    public void setAllBooks(List<Books> allBooks) {
        this.allBooks = allBooks;
    }

    /**
     * A list of the Books entity used to populate the data table that prints the books searched for in the database
     */
    private List<Books> listOfBooks;

    /**
     * Makes a call to the business logic which returns the list of books search for in the database, assigns this list to the listOfBooks list
     * @return returns the list of the Books searched for in the database
     */
    public List<Books> getListOfBooks() {
        listOfBooks = new ArrayList<>();
        listOfBooks = ss.findBooks(listOfBooks, author, title, yearOfPublication, priceRange);
        return listOfBooks;
    }

    public void setListOfBooks(List<Books> listOfBooks) {
        this.listOfBooks = listOfBooks;
    }

    /**
     * This is the object to the Administrator entity
     */
    private Administrator admin = new Administrator();

    public Administrator getAdmin() {
        return admin;
    }

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    /**
     * This is the Boolean variable responsible for rendering the administrator login button
     */
    private boolean adminRender;

    public boolean isAdminRender() {
        return adminRender;
    }

    public void setAdminRender(boolean adminRender) {
        this.adminRender = adminRender;
    }

    public void checkAdminCredentials() {
        if (admin.getUsername().equals("admin") && admin.getPassword().equals("admin")) {
            adminRender = true;
        }
    }

    /**
     * This function assigns the account name, status and time to the Orders entity object o because calling the business logic to place the order
     */
    public void placeOrder() {
        o.setAccountName(accountName);
        o.setStatus("Ordered");
        o.setOrderTime(LocalDateTime.now());
        ss.placeOrder(o);
    }

    /**
     * This is a List of the Orders entity, used to populate the data table that prints every transaction in the system
     */
    private List<Orders> transactions;

    /**
     * This retrieves all the transactions in the database through a call to the business logic and assigns the returned value to the transactions list
     * @return returns a List of every transaction in the system
     */
    public List<Orders> getTransactions() {
        transactions = ss.retrieveOrders();
        return transactions;
    }

    public void setTransactions(List<Orders> transactions) {
        this.transactions = transactions;
    }

    /**
     * This is the object of the Orders entity
     */
    private Orders o = new Orders();

    public Orders getO() {
        return o;
    }

    public void setO(Orders o) {
        this.o = o;
    }

    /**
     * The id of the order to be cancelled
     */
    private long deleteID;

    public long getDeleteID() {
        return deleteID;
    }

    public void setDeleteID(long deleteID) {
        this.deleteID = deleteID;
    }

    /**
     * This function calls the business logic to cancel an order
     */
    public void cancelOrder() {
        ss.cancelOrder(o, deleteID);
    }

    /**
     * This function calls the business logic to restock the books in the database
     */
    public void addBooks() {
        ss.addBooks();
    }

    /**
     * This function calls the business logic to update the status of an order
     */
    public void updatedOrderStatus() {
        String status = newStatus;
        ss.updatedStatus(o, status);
    }

    /**
     * This string variable is used to recover a forgotten email address
     */
    private String newEmailAddress;

    public String getNewEmailAddress() {
        return newEmailAddress;
    }

    public void setNewEmailAddress(String newEmailAddress) {
        this.newEmailAddress = newEmailAddress;
    }

    /**
     * This function calls the business logic to recover a forgotten email address
     */
    public void findEmailAddress() {
        newEmailAddress = ss.findEmailAddress(name, surname);
    }

    /**
     * This variable is used to recover a forgotten password
     */
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * This function calls the business logic to recover a forgotten password
     */
    public void findPassword() {
        newPassword = ss.findPassword(emailAddress);
    }

    /**
     * A Boolean variable used to render the data table that prints every order the account has placed
     */
    private boolean standardOrder = true;

    public boolean isStandardOrder() {
        return standardOrder;
    }

    public void setStandardOrder(boolean standardOrder) {
        this.standardOrder = standardOrder;
    }

    /**
     * A Boolean variable used to render the data table that prints every previous order involving a specific book the account has made
     */
    private boolean filterFlag;

    public boolean isFilterFlag() {
        return filterFlag;
    }

    public void setFilterFlag(boolean filterFlag) {
        this.filterFlag = filterFlag;
    }

    /**
     * This function filters the list of previous orders an account has made by a book title
     */
    public void filterOrders() {
        List<Orders> orders = ss.retrieveOrders();
        for (Iterator<Orders> iterator = orders.iterator(); iterator.hasNext();) {
            if (!iterator.next().getTitle().contains(orderFilter)) {
                iterator.remove();
            }
        }

        filteredOrder = orders;
        standardOrder = false;
        filterFlag = true;
    }

    /**
     * This function will hide the filtered orders data table, and render the all account orders data table
     */
    public void reset() {
        standardOrder = true;
        filterFlag = false;
    }

    /**
     * This list of the Orders entity is used to populate the filtered orders data table
     */
    private List<Orders> filteredOrder;

    public List<Orders> getFilteredOrder() {
        return filteredOrder;
    }

    public void setFilteredOrder(List<Orders> filteredOrder) {
        this.filteredOrder = filteredOrder;
    }

    /**
     * This String is used to filter all account orders to the filtered account orders
     */
    private String orderFilter;

    public String getOrderFilter() {
        return orderFilter;
    }

    public void setOrderFilter(String orderFilter) {
        this.orderFilter = orderFilter;
    }
    
    /**
     * This function filters every order in the database down to the orders placed by the logged in account
     * @return the list of account orders
     */
    public List<Orders> accountOrders() {
        String nameAcc = getAccountName();
        List<Orders> orders = ss.retrieveOrders();
        List<Orders> filteredOrders = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Orders orderNew = orders.get(i);
            String name = orderNew.getAccountName();
            if (orderNew.getAccountName() == null ? nameAcc == null : orderNew.getAccountName().equals(nameAcc)) {
                filteredOrders.add(orderNew);
            }
        }
        return filteredOrders;
    }

    /**
     * This list of the Orders entity is used to populate the all account orders data table
     */
    private List<Orders> orderList;

    /**
     * This function calls the accountOrders function and then assigns the list of account orders to the orderList list
     * @return returns the list of all account orders in the database
     */
    public List<Orders> getOrderList() {
        orderList = new ArrayList<>();
        List<Orders> orderListUpdate = accountOrders();
        for (int i = 0; i < orderListUpdate.size(); i++) {
            orderList.add(orderListUpdate.get(i));
        }
        return orderList;
    }

    public void setOrderList(List<Orders> orderList) {
        this.orderList = orderList;
    }

    /**
     * The Boolean variable to determine if the report of a single account order is rendered
     */
    private boolean selectFlag = false;

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    /**
     * This function renders the report of a single account order
     */
    public void showOrder() {
        selectFlag = true;
    }

    /**
     * This function hides the report of a single account order
     */
    public void hide() {
        selectFlag = false;
    }

    /**
     * A List of all the orders in the database
     */
    private List<Orders> adminOrders;

    /**
     * This function assigns all the orders in the database to the adminOrders List
     * @return returns the list of all the orders in the database
     */
    public List<Orders> getAdminOrders() {
        adminOrders = ss.retrieveOrders();
        return adminOrders;
    }

    public void setAdminOrders(List<Orders> adminOrders) {
        this.adminOrders = adminOrders;
    }

    /**
     * This variable is the specific transaction a report is being generated on
     */
    private Orders detailedTransaction;

    /**
     * This function is used to assign the selected transaction to the detailedTransaction variable
     * @return returns the specific transaction a report is generated on
     */
    public Orders getDetailedTransaction() {
        detailedTransaction = selectReport();
        return detailedTransaction;
    }

    public void setDetailedTransaction(Orders detailedTransaction) {
        this.detailedTransaction = detailedTransaction;
    }

//    public List<Orders> transactionReport() {
//        List<Orders> transactionList = ss.retrieveOrders();
//        List<Orders> oneTransaction = new ArrayList<>();
//        for (int i = 0; i < transactionList.size(); i++) {
//            if (transactionID == transactionList.get(i).getId()) {
//                transactionList.add(transactionList.get(i));
//            }
//        }
//        return oneTransaction;
//    }

    /**
     * A Boolean variable that determines whether the report of a specific transaction is rendered
     */
    private boolean transactionFlag = false;

    public boolean isTransactionFlag() {
        return transactionFlag;
    }

    public void setTransactionFlag(boolean transactionFlag) {
        this.transactionFlag = transactionFlag;
    }

    /**
     * This function renders the report of a specific transaction
     */
    public void showTransaction() {
        transactionFlag = true;
    }

    /**
     * This function filters the list of all transaction in the system down to the specific transaction identified
     * @return returns the specific transaction that a report is generated on
     */
    public Orders selectReport() {
        List<Orders> oneTransaction = ss.retrieveOrders();
        Orders od;
        for (int i = 0; i < oneTransaction.size(); i++) {
            od = oneTransaction.get(i);
            long enteredID = transactionID;
            if (od.getId().equals(enteredID)) {
                detailedTransaction = od;
            }
        }
        return detailedTransaction;
    }

    /**
     * A long variable used to identify a selected transaction
     */
    private long transactionID;

    public long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    /**
     * A long variable used to identify an order that is the focus of a status report
     */
    private long searchID;

    public long getSearchID() {
        return searchID;
    }

    public void setSearchID(long searchID) {
        this.searchID = searchID;
    }

    /**
     * A List of the Address entity, used to represent every delivery address assigned to the account logged in
     */
    private List<Address> deliveryAddresses;

    /**
     * This function calls the business logic to return all the delivery addresses of the current account, this is assigned to the deliveryAddresses list
     * @return returns the list of every delivery address of the current logged in account
     */
    public List<Address> getDeliveryAddresses() {
        deliveryAddresses = ss.accountAddress(getAccountName());
        return deliveryAddresses;
    }

    public void setDeliveryAddresses(List<Address> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
    }

    /**
     * A List of the PaymentMethod entity, used to show every payment method assigned to the logged in account
     */
    private List<PaymentMethod> paymentMethods;

    /**
     * This function calls the business logic to retrieve all the accounts payment method, this is assigned to the paymentMethods list
     * @return returns the list of every payment method of the current logged in account
     */
    public List<PaymentMethod> getPaymentMethods() {
        paymentMethods = ss.accountMethod(getAccountName());
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    /**
     * An order specified by its Id
     */
    private Orders selectedOrder;

    /**
     * Makes a call to the business logic to get the order in question, this order is assigned to the selectedOrder variable
     * @return returns the specified order
     */
    public Orders getSelectedOrder() {
        selectedOrder = ss.selectOrder(searchID, selectedOrder);
        return selectedOrder;
    }

    public void setSelectedOrder(Orders selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    /**
     * This function calls the business logic to add a book/multiple books to the BASKET table in the database
     */
    public void addToBasket() {
        ss.addBookToBasket(sb);
    }
    
    /**
     * The identifier of a book being added to the basket
     */
    private boolean bookID;

    public boolean isBookID() {
        return bookID;
    }

    public void setBookID(boolean bookID) {
        this.bookID = bookID;
    }

    public void shoppingBasket() {
        List<Basket> orderResult = ss.retrieveBasket();
        order = new ArrayList<>();
        for (int i = 0; i < orderResult.size(); i++) {
            Basket entry = orderResult.get(i);
            order.add(entry);
        }
    }

    /**
     * The object of the PaymentMethod entity
     */
    private PaymentMethod pay = new PaymentMethod();

    public PaymentMethod getPay() {
        return pay;
    }

    public void setPay(PaymentMethod pay) {
        this.pay = pay;
    }

    /**
     * The object of the Address entity
     */
    private Address a = new Address();

    public Address getA() {
        return a;
    }

    public void setA(Address a) {
        this.a = a;
    }

    /**
     * the Id of the payment method being used for the order
     */
    private long selectedPaymentMethod;

    public long getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }

    public void setSelectedPaymentMethod(long selectedPaymentMethod) {
        this.selectedPaymentMethod = selectedPaymentMethod;
    }

    /**
     * This function assigns the account name as the card holder of the payment method,
     * then calls the business logic to add the payment method to the database
     */
    public void addPaymentMethod() {
        pay.setCardHolder(getAccountName());
        ss.addPaymentMethod(pay, loginEmail, longTerm);
    }

    /**
     * This function assigns the account name as the customer of the address,
     * then calls the business logic to add the delivery address to the database
     */
    public void addDeliveryAddress() {
        a.setCustomer(getAccountName());
        ss.addDeliveryAddress(a, loginEmail, loginPassword);
    }

    /**
     * The list of books in the database's BASKET table
     */
    private List<Basket> order = new ArrayList<>();

    /**
     * This function retrieves the records of the BASKET table and assigns them to the list of books in the database's BASKET table
     * @return returns the list of books in the database's BASKET table
     */
    public List<Basket> getOrder() {
        order = new ArrayList<>();
        List<Basket> basketList = ss.retrieveBasket();
        for (int i = 0; i < basketList.size(); i++) {
            order.add(basketList.get(i));
        }
        return order;
    }

    public void setOrder(List<Basket> order) {
        this.order = order;
    }

    /**
     * The concatenation of the given and family name of the account
     */
    private String accountName;

    /**
     * The function calls the business logic to retrieve the full account name
     * @return returns the full account name
     */
    public String getAccountName() {
        accountName = ss.retrieveAccountName(loginName, loginSurname, loginEmail, loginPassword);
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    
    /**
     * The Boolean variable that renders the customer login button
     */
    private boolean render = false;

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    /**
     * The Boolean variable that renders the customer register button
     */
    private boolean customerFlag = false;

    public boolean isCustomerFlag() {
        return customerFlag;
    }

    public void setCustomerFlag(boolean customerFlag) {
        this.customerFlag = customerFlag;
    }

    /**
     * The Boolean variable that renders the administrator register button
     */
    private boolean administratorFlag = false;

    public boolean isAdministratorFlag() {
        return administratorFlag;
    }

    public void setAdministratorFlag(boolean administratorFlag) {
        this.administratorFlag = administratorFlag;
    }
    
    /**
     * The long term password entered when registering an account
     */
    private String longTerm;

    public String getLongTerm() {
        return longTerm;
    }

    public void setLongTerm(String longTerm) {
        this.longTerm = longTerm;
    }
    
    /**
     * The Boolean variable that renders the error message reminding users to fill in the fields of the register form
     */
    private boolean registerError;

    public boolean isRegisterError() {
        return registerError;
    }

    public void setRegisterError(boolean registerError) {
        this.registerError = registerError;
    }

    /**
     * This function saves the password entered when registering long term,
     * then it checks if any fields are empty and renders an error message if so,
     * if not it checks whether the account being registered is a customer or an administrator,
     * then the appropriate register button is rendered
     */
    public void checkRole() {
        longTerm = password;
        if(name.isEmpty() || surname.isEmpty() || emailAddress.isEmpty() || password.isEmpty()){
            customerFlag = false;
            administratorFlag = false;
            registerError = true;
        }
        else if(role.equals("Customer")){
            customerFlag = true;
            administratorFlag = false;
            registerError = false;
        }
        else
        {
            customerFlag = false;
            administratorFlag = true;
            registerError = false;
        }
        password = longTerm;
    }

    /**
     * This function calls the business logic which checks whether the login credentials are valid,
     * and what type of account they belong to, this is returned in a String,
     * if the credentials are valid a login button is rendered depending on whether the credentials are customer or administrator credentials
     */
    public void checkCredentials() {
        String type = ss.check(loginEmail, loginPassword);
        switch (type) {
            case "Customer":
                render = true;
                adminRender = false;
                loggedOutBrowser = false;
                break;
            case "Admin":
                render = false;
                adminRender = true;
                loggedOutBrowser = false;
                break;
            default:
                render = false;
                adminRender = false;
                break;
        }
    }
}
