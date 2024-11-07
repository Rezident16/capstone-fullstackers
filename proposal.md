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
│               │       DataException.java        -- data layer custom exception
│               │       PanelFileRepository.java  -- concrete repository
│               │       PanelRepository.java      -- repository interface
│               │
│               ├───domain
│               │       PanelResult.java          -- domain result for handling success/failure
│               │       PanelService.java         -- panel validation/rules
│               │
│               ├───models
│               │       Material.java             -- enum representing the 5 materials
│               │       Panel.java                -- solar panel model
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
                │       PanelFileRepositoryTest.java    -- PanelFileRepository tests
                │       PanelRepositoryTestDouble.java  -- helps tests the service, implements PanelRepository
                │
                └───domain
                        PanelServiceTest.java           -- PanelService tests
```
