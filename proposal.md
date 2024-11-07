# FullStackers Proposal - Project Stock Connect

## Team 
    - Andrei Vorobev
    - Mely Espino
    - Lance Jabril Tero
    - Shilpa Sivarajan

# Problem Statement
People interested in stock trading often struggle to stay informed about the stocks they care about and to communicate effectively with others about those stocks. Information is scattered across multiple apps, sites, and social media platforms, making it difficult to have a focused, real-time discussion or exchange valuable insights about stocks without distraction.

# Technical Solution
Anna is a new investor interested in technology stocks. She uses the Stock Connect app to create a watchlist of her favorite stocks and receives real-time updates and joins a chat group within the app, dedicated solely to one of her stocks, where experienced investors share tips and analysis. This focused space helps her learn without the distraction of irrelevant discussions or spam, allowing her to gain confidence and connect with knowledgeable investors who share her goals.

# Glossary
- User
    - Anyone using the app
- Admin
    - Specific users who have more power on the app. They can add a stock channel and delete other users messages.
- Stock
    - A channel that will be showcased on the left side of the screen that contains a stocks name and will have discussion about that stock
- Likes
    - A reaction to a message someone can anyone can like a message and a message will show how many people have liked it
- Messages
    - A text that user can send in chat for a specific stock and will be displayed.

# High Level Requirements

- Create a message by the user (USER)
- Edit the message by the user (USER)
- Delete the message by the user (USER/ADMIN)
- Display the stocks for that specific chat (authenticated)
- Display likes/dislikes for the message post (authenticated)
- Add a like/dislike to (USER)
- Update Like/Dislike (user)
- Create Stock(User/Admin)
- Update Stock(Admin)
- Delete Stock(Admin)

# User Stories

## Messages

### User wants to send a message
 - Suggested data:
    - Apple has been killing it lately! HODL!!

- Precondition:
    - The User must be logged in

### Edit a message
- Suggested data:
    - Apple has been killing it today! HODL!!
- Precondition:
    - The User must be logged in
    - The user must be the owner of the message

### Delete a message
- Precondition:
    - The user must be logged in
    - The user must be an owner of the message or ADMIN

## Stocks

### User want to add a stock
- Suggested Data:
    - Ticker: AAPL
    - Name: Apple
    - Descirption: generic description for the stock
- Precondition:
    - The user must be logged in
    - The Ticker can't be duplicate
- Postcondition:
    - The admin must first approve the stock. Once approved, the stock is created and users are able to navigate to it.

### User want to update the stock
- Suggested Data:
    - Name: Apple Inc.
    - Description: Updated Descirption
- Precondition:
    - The user must be an admin

### Delete a stock
- Precondition:
    - The user must be an admin

## Likes

### User wants to like a message:
- Precondition:
    - The user must be logged in
    - The message must exist
    - The user can't have like for that specific message

### User wants to change like to dislike
- Precondition:
    - The like should exist
    - The user must be logged in
    - The user must be the one who liked the message

### User wants to delete a like
- Precondition:
    - The like should exist
    - The user must be logged in
    - The user must be the one who liked the message

# Database

![image](https://github.com/user-attachments/assets/6937751a-4c41-49bf-bd3c-66064e70c5d5)


## Validation

### User

- **user_email** is required and cannot be blank.
- **user_password** is required and cannot be blank.
- **username** is required and cannot be blank.

### Stocks
- **name** is required and cannot be blank.
- **descirption** is required and cannot be blank.
- **ticker** is required and cannot be blank.

### Messages (Posts)
- **content** is required and cannot be blank.
- **date_posted** is required and cannot be blank.

### Likes/Dislikes
- **isLiked** is required and cannot be blank.

# Class Diagram
```
src
├───main
│   └───java
│       └───learn
│           └───stockConnect
│               │   App.java                      -- app entry point
│               │
│               ├───data
│               │       DataException.java
│               │       UserFileRepository.java
│               │       UserRepository.java
│               │       MessageFileRepository.java
│               │       MessageRepository.java
│               │       StockFileRepository.java
│               │       StockRepository.java
│               │       UserStockFileRepository.java
│               │       UserStockRepository.java
│               │       LikeFileRepository.java
│               │       LikeRepository.java
│               │
│               ├───domain
│               │       Result.java          -- domain result for handling success/failure
│               │       UserService.java
│               │       MessageService.java
│               │       StockService.java
│               │       UserStocksService.java
│               │       LikeService.java
│               │
│               ├───models
│               │       User.java
│               │       Message.java
│               │       Stock.java
│               │       UserStock.java
│               │       Like.java
│               │
│               └───ui
│                       Controller.java           -- UI controller
│                       View.java                 -- all console input/output
│
└───test
    └───java
        └───learn
            └───stockConnect
                ├───data
                │       UserFileRepositoryTest.java
                │       UserRepositoryTestDouble.java
                │       MessageFileRepositoryTest.java
                │       MessageRepositoryTestDouble.java
                │       StockFileRepositoryTest.java
                │       StockRepositoryTestDouble.java
                │       UserStockFileRepositoryTest.java
                │       UserStockRepositoryTestDouble.java
                │       LikeFileRepositoryTest.java
                │       LikeRepositoryTestDouble.java
                │
                └───domain
                │       UserServiceTest.java
                │       MessageServiceTest.java
                │       StockServiceTest.java
                │       UserStocksServiceTest.java
                │       LikeServiceTest.java
```




# Class Details

## App
- `public static void main(String[])` -- instantiate all required classes with valid arguments, dependency injection. run controller

## data.DataException

Custom data layer exception.

- `public DataException(String, Throwable)` -- constructor, Throwable arg is the root cause exception


## Repositories

### data.UserFileRepository
- `private String filePath`
- `public List<User> findById(int)` -- finds the user by id
- `public List<User> findAll()` -- finds all users
- `public User findByUsername()` -- finds user by username
- `public User findByEmail()` -- finds user by email
- `public User add(User)` -- create a User
- `public boolean update(User)` -- update a user
- `public boolean deleteById(int)` -- delete a User by its id
- `private String serialize(User)` -- convert a User into a String (a line) in the file
- `private User deserialize(User)` -- convert a String into a User


- `public List<Stock> findStockByUserId(int)` - find list of watchlist(favorite) stocks from UserStock by Id

### data.UserRepository (interface)

Contract for UserFileRepository and UserRepositoryTestDouble.

- `public List<User> findById(int)`
- `public List<User> findAll()`
- `public User findByUsername()`
- `public User findByEmail()`
- `public User add(User)`
- `public boolean update(User)`
- `public boolean deleteById(int)`


### data.MessageFileRepository
- `private String filePath`
- `public Message findById(int)` -- finds the Message by id
- `public List<Message> findByUserId(int)` -- finds the Messages by userId
- `public List<Message> findByStockId(int)` -- finds the Messages by stockId
- `public Message add(Message)` -- add message
- `public boolean update(message)` -- update message
- `public boolean deleteById(int)` -- delete message by id
- `private String serialize(Message)` -- convert a Message into a String (a line) in the file
- `private Message deserialize(Message)` -- convert a String into a Message

### data.MessageRepository
- `public Message findById(int)`
- `public List<Message> findByUserId(int)`
- `public List<Message> findByStockId(int)`
- `public Message add(Message)`
- `public boolean update(message)`
- `public boolean deleteById(int)`

### data.StockFileRepository
- `private String filePath`
- `public Stock findById(int)` -- find by id
- `public Stock findByTicker(String)` -- find By Ticker
- `public Stock add(Stock)` -- add Stock
- `public boolean update(Stock)` -- update Stock
- `public boolean deleteById(int)` -- delete Stock by id
- `private String serialize(Stock)` -- convert a Stock into a String (a line) in the file
- `private Stock deserialize(Stock)` -- convert a String into a Stock


### data.StockRepository
- `public Stock findById(int)`
- `public Stock findByTicker(String)`
- `public Stock add(Stock)`
- `public boolean update(Stock)` 
- `public boolean deleteById(int)`

### data.UserStockFileRepository (Favorite Stocks/Watchlist)
- `private String filePath`
- `public UserStock findById(int)` - find by id
- `public boolean deleteById(int)` -- delete UserStock by id
- `public UserStock add(UserStock)` -- add UserStock
- `private String serialize(UserStock)` -- convert a UserStock into a String (a line) in the file
- `private UserStock deserialize(UserStock)` -- convert a String into a UserStock

### data.UserStockRepository.java
- `public UserStock findById(int)`
- `public boolean deleteById(int)`
- `public UserStock add(UserStock)`

### data.LikeFileRepository.java
- `private String filePath`
- `public Like findById(int)` - find by id
- `public Like findByUserId(int)` - findy by user id
- `public Like findByMessageId(int)` - findy by message id
- `public Like updateLike(int)` - update the Like by id

### data.LikeRepository.java
- `public Like findById(int)` 
- `public Like findByUserId(int)` 
- `public Like findByMessageId(int)` 
- `public Like updateLike(int)`

### domain.Result
- `private ArrayList<String> messages` -- error messages
- `private Panel panel` -- an optional Panel
- `public boolean isSuccess()` -- calculated getter, true if no error messages
- `public List<String> getMessages()` -- messages getter, create a new list
- `public Panel getPanel()` -- Panel getter
- `public void setPanel(Panel)` -- Panel setter
- `public void addMessage(String)` -- adds an error message to messages



### domain.UserService
-  `private UserRepository repository` -- required data dependency
-  `public UserService(UserRepository)` -- constructor
-  `public Result add(User)` -- validate, then add via repository
-  `public Result update(User)` -- validate, then update via repository
-  `public Result deleteById(int)` -- pass-through to repository
-  `private Result validate(User)` -- general-purpose validation routine

### domain.Message
-  `private MessageRepository repository` -- required data dependency
-  `public MessageService(MessageRepository)` -- constructor
-  `public List<Message> findByStockId()` -- pass-through to repository
-  `public Result add(Message)` -- validate, then add via repository
-  `public Result update(Message)` -- validate, then update via repository
-  `public Result deleteById(int)` -- pass-through to repository
-  `private Result validate(Message)` -- general-purpose validation routine


### domain.Stock
-  `private StockRepository repository` -- required data dependency
-  `public StockService(StockRepository)` -- constructor
-  `public Result add(Stock)` -- validate, then add via repository
-  `public Result update(Stock)` -- validate, then update via repository
-  `public Result deleteById(int)` -- pass-through to repository
-  `private Result validate(Stock)` -- general-purpose validation routine

### domain.UserStocksService
- `public Result findById(int)`
- `public boolean deleteById(int)`
- `public Result add(UserStock)`
- `private Result validate(UserStock)` -- general-purpose validation routine

### domain.LikeService
-  `public Result add(Like)` -- validate, then add via repository
-  `public Result update(Like)` -- validate, then update via repository
-  `public Result deleteById(int)` -- pass-through to repository
-  `private Result validate(Like)` -- general-purpose validation routine (edited) 


### models.User
- `private int user_id`
- `private String user_first_name`
- `private String user_last_name`
- `private String username`
- `private String email`
- `private String password`

### models.Message
- `private int message_id`
- `private int user_id`
- `private int stock_id`
- `private String content`
- `private date date_of_post`

### models.Stock
- `private int stock_id`
- `private String stock_name`
- `private String description`
- `private String ticker`

### models.UserStock
- `private int user_stock_id`
- `private int user_id`
- `private int stock_id`

### models.Like
- `private int like_id`
- `private boolean isliked`
- `private int user_id`
- `private int message_id`

### controllers.User
- `addUser(User user)`
- `updateUser(User user)`
- `deleteUserById(int userId)`
- `findUserByUsername(String username)`
- `findUserByEmail(String email)`

### controllers.Messages
- `addMessage(Message message)`
- `findMessagesByUserId(int userId)`
- `findMessagesByStockId(int stockId)`


### controllers.Stocks
- `addStock(Stock stock)`
- `updateStock(Stock stock)`
- `deleteStockById(int stockId)`
- `findStockByTicker(String ticker)`

### controllers.Likes
- `addLike(Like like)`
- `updateLike(Like like)`
- `findLikesByUserId(int userId)`
- `findLikesByMessageId(int messageId)`

### controllers.UserStock
- `addUserStock(UserStock userStock)`
- `deleteUserStockById(int userStockId)`
- `findUserStocksByUserId(int userId)` (edited) 

### ui.View
- `private Scanner console` -- a Scanner to be used across all methods
- `public int chooseOptionFromMenu()` -- display the main menu and select an option, used to Controller.run()
- `public void printHeader(String)` -- display text to the console with emphasis
- `public void printResult(PanelResult)` -- display a PanelResult with either success or failure w/ messages included
- `public void printPanels(String sectionName, List<Panel>)` -- display panels in a section with the section's name
- `public Panel choosePanel(String sectionName, List<Panel>)` -- choose a panel from a list of options (useful for update and delete)
- `public Panel makePanel()` -- make a panel from scratch, used in Controller.addPanel
- `public Panel update(Panel)` -- accept and existing Panel, update it, and return it, used in Controller.updatePanel
- `public String readSection()` -- reads a section name, used in Controller: viewBySection, updatePanel (must search first), and deletePanel
- `private String readString(String)` -- general-purpose console method for reading a string
- `private String readRequiredString(String)` -- general-purpose console method for reading a required string
- `private int readInt(String)` -- general-purpose console method for reading an integer
- `private int readInt(String, int min, int max)` -- general-purpose console method for reading an integer with a min and max
- `private Material readMaterial()` -- domain-specific console method for choosing a Material


# Steps

## Backend

### Monday

- Set Up Maven Project (~30 min)
    - Create a Maven project.
    - Add jUnit 5, Jupiter, as a Maven dependency and refresh Maven
    - Create packages.
- Create Models (30min - 1hour per model; Up to 2 hours)
    - Create the `User` model.
    - Create the `Stocks` model.
    - Create the `Likes` model.
    - Create the `Message` model.

- Implement `Security`(1 hour)
- Create Repositories:
    - All methods should catch IOExceptions and throw `DataException`.
    
    - Create the `UserFileRepository` class. (2 hours)
        - Create the data layer's custom `DataException`
        - add the filePath field and create a constructor to initialize the field
        - generate tests for `UserFileRepository`, should be located in `src/test/java/learn/stockConnect/data/UserFileRepositoryTest`
        - create a `data` directory in the project root. add test, seed, and production data files
        - implement `serialize`, and `deserialize`. these are all private method. 
        - implement `findById`, `findAll`, `findByUsername`, `findByEmail` 
        - implement `add`
        - improve tests by establishing known-good-state with `@BeforeAll`
        - test `add`
        - implement `update`
        - test `update`
        - implement `deleteById`
        - test `deleteById`

- Create the `MessageFileRepository` class. (2 hours)
    All methods should catch IOExceptions and throw `DataException`.
    - add the filePath field and create a constructor to initialize the field
    - generate tests for `MessageFileRepository`, should be located in `src/test/java/learn/stockConnect/data/MessageFileRepositoryTest`
    - create a `data` directory in the project root. add test, seed, and production data files
    - implement `serialize`, and `deserialize`. these are all private method. 
    - implement `findById`, `findByStockId`, `findByUserId` 
    - implement `add`
    - improve tests by establishing known-good-state with `@BeforeAll`
    - test `add`
    - implement `update`
    - test `update`
    - implement `deleteById`
    - test `deleteById`

- Create the `StockFileRepository` class. (2 hours)

    All methods should catch IOExceptions and throw `DataException`.

    - add the filePath field and create a constructor to initialize the field
    - generate tests for `StockFileRepository`, should be located in `src/test/java/learn/stockConnect/data/StockFileRepositoryTest`
    - create a `data` directory in the project root. add test, seed, and production data files
    - implement `serialize`, and `deserialize`. these are all private method. 
    - implement `findById`, `findByTicker`
    - implement `add`
    - improve tests by establishing known-good-state with `@BeforeAll`
    - test `add`
    - implement `update`
    - test `update`
    - implement `deleteById`
    - test `deleteById`

- Create the `UserStockFileRepository` class. (2 hours)

    All methods should catch IOExceptions and throw `DataException`.

    - add the filePath field and create a constructor to initialize the field
    - generate tests for `UserStockFileRepository`, should be located in `src/test/java/learn/stockConnect/data/UserStockFileRepositoryTest`
    - create a `data` directory in the project root. add test, seed, and production data files
    - implement `serialize`, and `deserialize`. these are all private method. 
    - implement `findById`
    - implement `add`
    - improve tests by establishing known-good-state with `@BeforeAll`
    - test `add`
    - implement `update`
    - test `update`
    - implement `deleteById`
    - test `deleteById`

- Create the `LikeFileRepository` class. (2 hours)

    All methods should catch IOExceptions and throw `DataException`.

    - add the filePath field and create a constructor to initialize the field
    - generate tests for `LikeFileRepository`, should be located in `src/test/java/learn/stockConnect/data/LikeFileRepositoryTest`
    - create a `data` directory in the project root. add test, seed, and production data files
   
    - implement `findById`, `findByUserId`, `findByMessageId`
    - implement `updateLike`
    - improve tests by establishing known-good-state with `@BeforeAll`
    - test `updateLike`

### Tuesday

- Create Service (up to 2 hours per service)
    #### User Service
    - add a `UserRepository` (interface) field with a corresponding constructor
    - generate tests for `UserService`
    - create `UserRepositoryTestDouble` to support service testing, this test class implements `UserRepository`
    - implement `findById` and test, implement just enough code in `UserRepositoryTestDouble` to enable service testing
    - implement `findByUsername` and test
    - implement `findByEmail` and test
    - implement `add` and test, requires validation
    - implement `update` and test, requires validation
    - implement `deleteById` and test


    #### Message Service
    - add a `MessageRepository` (interface) field with a corresponding constructor
    - generate tests for `MessageService`
    - create `MessageRepositoryTestDouble` to support service testing, this test class implements `MessageRepository`
    - implement `findById` and test, implement just enough code in `MessageRepositoryTestDouble` to enable service testing
    - implement `findByUserId` and test
    - implement `findByStockId` and test
    - implement `add` and test, requires validation
    - implement `update` and test, requires validation
    - implement `deleteById` and test


    #### Stock Service
    - add a `StockRepository` (interface) field with a corresponding constructor
    - generate tests for `StockService`
    - create `StockRepositoryTestDouble` to support service testing, this test class implements `StockRepository`
    - implement `findById` and test, implement just enough code in `StockRepositoryTestDouble` to enable service testing
    - implement `findByTicker` and test
    - implement `add` and test, requires validation
    - implement `update` and test, requires validation
    - implement `deleteById` and test

    #### Like Service
    - add a `LikeRepository` (interface) field with a corresponding constructor
    - generate tests for `LikeService`
    - create `LikeRepositoryTestDouble` to support service testing, this test class implements `LikeRepository`
    - implement `findById` and test, implement just enough code in `LikeRepositoryTestDouble` to enable service testing
    - implement `findByUserId` and test
    - implement `findByMessageId` and test
    - implement `update` and test, requires validation
    - implement `deleteById` and test

    #### UserStock Service
    - add a `UserStockRepository` (interface) field with a corresponding constructor
    - generate tests for `UserStockService`
    - create `UserStockRepositoryTestDouble` to support service testing, this test class implements `UserStockRepository`
    - implement `findById` and test, implement just enough code in `UserStockRepositoryTestDouble` to enable service testing
    - implement `add` and test, requires validation
    - implement `deleteById` and test

- Create Result (up to 1 hour per result)
    - Stock
    - Messages
    - Likes/Dislikes
    - UserStock
    - User

- Controller
    - Security Controller (Monday)
    - Mappers (30 mins per person)
    - Controller per each of the tables (1 hour per person)

- Websockets
    - Implement websockets on the backend(up to 2 hours) for the messages

### Backend should be complete by Tuesday
==================================================================================

## Frontend
### Wednesday
- Set Up websockets front end (1 hour)
1. Set up index.js and Main.js
2. Set up fetch functions:
    - Users - 1 hour
    - Stocks - 1 hour
    - UserStocks - 30 mins
    - Messages - 1 hour
    - Likes - 30 mins

3. Set up components
    - Create Update Forms
        - User - 1 hour
        - Stock - 1 hour
        - Messages - 1 hour
        - UserStocks/Likes - just a button to add/remove or like/dislike/remove - 1 hour

    - Display User - 1 hour
    - Display Stock Chat - 1 hour
        -  Display Messages - 1 hour
            - Display Likes - 1 hour

    - Navigation - 1 hour
    - Stock Chart - 1 hour

### FrontEnd Perspectives

- ** Sign In Page **
    - Display sign in form on this page
    - Collect user info from user through the displayed input fields
    - Validate the info entered by the user and show errors on top if any
    - Take the user to the Stock Chat Page if there are no errors

- ** Stock Chat Page **
    - Display the navigation bar on the left for the user to navigate through different stocks
    - Display the current status of the stock on top through a moving graph
    - Display a posts from other users that have been already posted below the graph
    - Display an input field for the user to enter new posts along with the post button
    - Collect the user input and display it as a post along with the previous posts
    - Display edit and delete button options for the user to edit or delete the message post posted by the user
    - Edit button takes the user to the form with his post in the input field where the user can edit the message
    - Delete button will confirm with the user id the message should be deleted and then deletes it if the user clicks okay

    