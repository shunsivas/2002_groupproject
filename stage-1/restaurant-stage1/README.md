# Restaurant Manager

Console game: basic restaurant operation (Regular customers, Salad/Burger/Pasta, Waiter, Chef, Cashier, 2 tables, 18 turns, victory/defeat). 

## Build / run 
    ./build.sh
    ./run.sh 

(Windows: `javac -d out (all .java under src)` then `java -cp out restaurant.Main`.)

## Commands
- Waiter (one task/turn): `seat <customer> [table]`, `take <customer>`, `serve <customer>`, `wait`
- Chef (one unit/turn): `cook <customer>`, `wait`
- `help`, `quit`. Customers are numbered: C1 = `1`.
- Invalid choices print the reason and ask again; the task is not used up.

## Rules implemented (base setting)
18 turns, 2 tables, arrivals at turns 1,4,7,10,13,16, goal: >=4 paid AND >= $45.00 AND avg satisfaction >= 60.
Each turn: arrivals -> manager choices (Waiter first, then Chef; Chef may cook an order taken this turn) ->
kitchen/serving (a dish finished this turn can only be served next turn) -> eating & automatic Cashier
(oldest first; newly READY_TO_PAY pays next turn) -> waiting loss (Regular 8, min 0, abandonment at 0 cancels
order and frees table) -> result after turn 18 (no early ending).

**Customer food choice (documented rule):** seeded `java.util.Random`, uniform over Salad/Burger/Pasta, drawn
when the customer arrives, shown when the order is taken. 
Money is stored as integer cents.

## Structure
- `restaurant.domain`  - MenuItem, Customer, Order, Table, Waiter, Chef, Cashier, Restaurant (rules for entities)
- `restaurant.control` - GameController (turn sequence, win/lose), GameConfig, AutoPlayer
- `restaurant.ui`      - ConsoleUI (input/display only, no rules)
