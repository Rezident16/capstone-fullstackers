# FullStackers Proposal

### High Level Requirements

- Create a message by the user
- Edit the message by the user
- Delete the message by the user
- Display the messages by the user
- Display the stocks for that specific chat
- Display likes/dislikes for the message post
- Add a like/dislike to

# Database

![image](https://github.com/user-attachments/assets/e50130bc-51cc-4853-82a4-ae284becaeda)

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

# Technical Requirements

- Three layer architecture
- Data stored in a delimited file.
- Repositories should throw a custom exception, never file-specific exceptions.
- Repository and service classes must be fully tested with both negative and positive cases. Do not use your "production" data file to test your repository.
- Must have at least 2 roles (example User and Admin)
- Sensible layering and pattern choices
- Spring Boot, MVC, JDBC, Testing, React
- An HTML and CSS UI that's built with React
- MySQL for data management
- Manage 4-7 database tables (entities) that are independent concepts. A simple bridge table doesn't count.



# Class Diagram
```
src
├───main
│   └───java
│       └───learn
│           └───solar
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
            └───solar
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

### data.LikeRepository.java







### domain.PanelResult
- `private ArrayList<String> messages` -- error messages
- `private Panel panel` -- an optional Panel
- `public boolean isSuccess()` -- calculated getter, true if no error messages
- `public List<String> getMessages()` -- messages getter, create a new list
- `public Panel getPanel()` -- Panel getter
- `public void setPanel(Panel)` -- Panel setter
- `public void addMessage(String)` -- adds an error message to messages

### domain.PanelService
-  `private PanelRepository repository` -- required data dependency
-  `public PanelService(PanelRepository)` -- constructor
-  `public List<Panel> findBySection(String)` -- pass-through to repository
-  `public PanelResult add(Panel)` -- validate, then add via repository
-  `public PanelResult update(Panel)` -- validate, then update via repository
-  `public PanelResult deleteById(int)` -- pass-through to repository
-  `private PanelResult validate(Panel)` -- general-purpose validation routine

### models.Material

An enum with five values: multicrystalline silicon, monocrystalline silicon, amorphous silicon, cadmium telluride, copper indium gallium selenide. Can use industry abbreviations or full names.

### models.Panel
- `private int id`
- `private String section`
- `private int row`
- `private int column`
- `private int installationYear`
- `private Material material`
- `private boolean tracking`
- Full getters and setters
- override `equals` and `hashCode`

### ui.Controller
- `private View view` -- required View dependency
- `private PanelService service` -- required service dependency
- `public Controller(View, PanelService)` -- constructor with dependencies
- `public void run()` -- kicks off the whole app, menu loop
- `private void viewBySection()` -- coordinates between service and view to display panels in a section
- `private void addPanel()` -- coordinates between service and view to add a new panel
- `private void updatePanel()` -- coordinates between service and view to update a panel
- `private void deletePanel()` -- coordinates between service and view to delete a panel

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

## Steps

1. Create a Maven project.
2. Add jUnit 5, Jupiter, as a Maven dependency and refresh Maven
3. Create packages.
4. Create the `Panel` model.
5. Create the `Material` enum.
6. Create the data layer's custom `DataException`
7. Create the `PanelFileRepository` class.

    All methods should catch IOExceptions and throw `DataException`.

    - add the filePath field and create a constructor to initialize the field
    - generate tests for `PanelFileRepository`, should be located in `src/test/java/learn/solar/data/PanelFileRepositoryTest`
    - create a `data` directory in the project root. add test, seed, and production data files
    - implement `findAll`, `serialize`, and `deserialize`. these are all private method. may be useful to make `findAll` public temporarily and test it quick.
    - implement `findBySection`, it uses `findAll`. test it naively (no known-good-state for now)
    - implement `add`
    - improve tests by establishing known-good-state with `@BeforeAll`
    - test `add`
    - implement `update`
    - test `update`
    - implement `deleteById`
    - test `deleteById`

8. Extract the `PanelRepository` interface (IntelliJ: Refactor -> Extract Interface) from `PanelFileRepository`.
9. Create `PanelResult`.
10. Create `PanelService`.

    - add a `PanelRepository` (interface) field with a corresponding constructor
    - generate tests for `PanelService`
    - create `PanelRepositoryTestDouble` to support service testing, this test class implements `PanelRepository`
    - implement `findBySection` and test, implement just enough code in `PanelRepositoryTestDouble` to enable service testing
    - implement `add` and test, requires validation
    - implement `update` and test, requires validation
    - implement `deleteById` and test

11. Create `View`

    - add `Scanner` field
    - create read* methods: `readString`, `readRequiredString`, `readInt`, `readInt` (with min/max)

12. Create `Controller`

    - add fields for service and view with corresponding constructor
    - add a `run` method

13. Create `App` and the `main` method.

    - instantiate all required objects: repository, service, view, and controller
    - run the controller

14. Work back and forth between controller and view.

    Run early and often. Add `System.out.println`s as breadcrumbs in controller, but don't forget to remove them when development is complete.

    Determine the correct sequence for service calls and view calls. What is the order?

    - implement `chooseOptionFromMenu` and `printHeader` in view
    - use them in the controller's `run`
    - implement `viewBySection` in controller, complete required view methods: `readSection`, `printPanels`
    - implement `addPanel` in controller, complete required view methods: `makePanel`, `readMaterial`, `printResult`
    - implement `updatePanel` in controller, complete required view methods: `choosePanel`, `update`
    - implement `deletePanel` in controller, complete required view methods (`deletePanel` can re-use `choosePanel`)

## Controller Perspectives

### View Panels by Section
1. collect section name from the view
2. use the name to fetch panels from the service
3. use the view to display panels

### Add a Panel
1. collect a complete and new panel from the view
2. use the service to add the panel and grab its result
3. display the result in the view

### Update a Panel
1. collect section name from the view
2. use the name to fetch panels from the service
3. display the panels in the view and allow the user to choose a panel (if no panel selected, abort)
4. update panel properties (setters) in the view
5. use the service to update/save the panel and grab its result
6. display the result in the view

### Delete a Panel
1. collect section name from the view
2. use the name to fetch panels from the service
3. display the panels in the view and allow the user to choose a panel (if no panel selected, abort)
4. use the service to delete the panel by its identifier
5. display success or failure in the view
