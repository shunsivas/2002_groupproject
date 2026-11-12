package restaurant.ui;

import restaurant.control.*;
import restaurant.domain.*;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;


public class ConsoleUI {
    private final GameController game;
    private final Scanner in;
    private final PrintStream out;

    public ConsoleUI(GameController game, Scanner in, PrintStream out) {
        this.game = game;
        this.in = in;
        this.out = out;
    }

    public boolean run() {
        GameConfig cfg = game.config();
        out.println(" Restaurant Manager");
        out.printf("%d turns, %d tables, seed %d. Goal: >= %d paid, revenue >= %s, avg satisfaction >= %d.%n",
                cfg.maxTurns(), cfg.tables(), cfg.seed(), cfg.minPaid(),
                Money.format(cfg.minRevenueCents()), cfg.minAvgSatisfaction());
        out.println("Type 'help' for commands.");
        while (!game.isOver()) {
            List<String> arrivals = game.beginTurn();
            showState(arrivals);
                if (!askStaff(true)) return false;
                out.println("Chef tasks (after the Waiter's choice): " + taskList(game.availableChefTasks()));
                if (!askStaff(false)) return false;

            List<String> events = game.endTurn();
            out.println("-- End of turn " + game.turn() + " --");
            for (String e : events) out.println("  * " + e);
        }
        showFinal();
        return true;
    }

    private void showState(List<String> arrivals) {
        Restaurant r = game.restaurant();
        out.println();
        out.println("================ TURN " + game.turn() + " / " + game.config().maxTurns() + " ================");
        for (String a : arrivals) out.println("  * " + a);
        out.println("Revenue: " + Money.format(r.revenueCents()) + "   Avg satisfaction (paid): " + avg(r)
                + "   Paid: " + r.paidCount());
        out.print("Waiting queue: ");
        if (r.queue().isEmpty()) out.println("(empty)");
        else {
            StringBuilder sb = new StringBuilder();
            for (Customer c : r.queue()) sb.append(c.label()).append(" ").append(c.typeLabel()).append("[sat ").append(c.satisfaction()).append("] ");
            out.println(sb.toString().trim());
        }
        for (Table t : r.tables()) {
            Customer c = t.occupant();
            if (c == null) out.println("Table " + t.number() + ": free");
            else out.println("Table " + t.number() + ": " + c.label() + " " + c.typeLabel() + " [sat " + c.satisfaction() + ", "
                    + c.state() + "]" + (c.order() != null ? " order: " + c.order() : " no order yet"));
        }
        out.println("Waiter tasks: " + taskList(game.availableWaiterTasks()));
        out.println("Chef tasks:   " + taskList(game.availableChefTasks()));
    }

    private String taskList(List<String> tasks) {
        if (tasks.isEmpty()) return "wait only";
        return "\n    " + String.join("\n    ", tasks) + "\n    wait";
    }

    private String avg(Restaurant r) {
        Double a = r.averageSatisfaction();
        return a == null ? "N/A" : String.format("%.1f", a);
    }

    /** Asks for the Waiter's (waiter=true) or Chef's choice until valid, wait, or quit. */
    private boolean askStaff(boolean waiter) {
        String role = waiter ? "Waiter" : "Chef";
        while (true) {
            out.print(role + "> ");
            out.flush();
            if (!in.hasNextLine()) { out.println("\nInput ended; game aborted."); return false; }
            String line = in.nextLine().trim().toLowerCase();
            if (line.isEmpty()) continue;
            String[] p = line.split("\\s+");
            switch (p[0]) {
                case "quit": out.println("Game aborted."); return false;
                case "help": help(); continue;
                case "wait": out.println("  " + role + " waits."); return true;
                default: break;
            }
            ActionResult res = dispatch(waiter, p);
            out.println("  " + (res.ok() ? "" : "INVALID: ") + res.message());
            if (res.ok()) return true;
            out.println("  (nothing was used up; choose again)");
        }
    }

    private ActionResult dispatch(boolean waiter, String[] p) {
        try {
            if (waiter) {
                switch (p[0]) {
                    case "seat":
                        if (p.length < 2 || p.length > 3) return ActionResult.fail("Usage: seat <customer> [table]");
                        return game.waiterSeat(Integer.parseInt(p[1]), p.length == 3 ? Integer.parseInt(p[2]) : null);
                    case "take":
                        if (p.length != 2) return ActionResult.fail("Usage: take <customer>");
                        return game.waiterTakeOrder(Integer.parseInt(p[1]));
                    case "serve":
                        if (p.length != 2) return ActionResult.fail("Usage: serve <customer>");
                        return game.waiterServe(Integer.parseInt(p[1]));
                    default:
                        return ActionResult.fail("Unknown Waiter command '" + p[0] + "'. Type 'help'.");
                }
            }
            if (p[0].equals("cook")) {
                if (p.length != 2) return ActionResult.fail("Usage: cook <customer>");
                return game.chefCook(Integer.parseInt(p[1]));
            }
            return ActionResult.fail("Unknown Chef command '" + p[0] + "'. Type 'help'.");
        } catch (NumberFormatException e) {
            return ActionResult.fail("Customer and table must be numbers (e.g. 'seat 1 2').");
        }
    }

    private void help() {
        out.println("  Waiter (one task per turn):  seat <customer> [table] | take <customer> | serve <customer> | wait");
        out.println("  Chef   (one unit per turn):  cook <customer> | wait");
        out.println("  Customers are numbered (C1 = 1). 'quit' aborts the game.");
    }

    private void showFinal() {
        Restaurant r = game.restaurant();
        Outcome o = game.result();
        GameConfig cfg = game.config();
        out.println();
        out.println(" RESULT ");
        out.println("Served:             " + r.servedCount());
        out.println("Paid customers:     " + r.paidCount() + " (need " + cfg.minPaid() + ")  " + mark(o.paidOk()));
        out.println("Unhappy departures: " + r.unhappyDepartures());
        out.println("Revenue:            " + Money.format(r.revenueCents()) + " (need "
                + Money.format(cfg.minRevenueCents()) + ")  " + mark(o.revenueOk()));
        out.println("Avg satisfaction:   " + avg(r) + " (need " + cfg.minAvgSatisfaction() + ")  "
                + mark(o.satisfactionOk()));
        out.println("Turns used:         " + game.turn());
        out.println(o.victory() ? "* VICTORY *" : "* DEFEAT *");
    }

    private String mark(boolean ok) { return ok ? "[met]" : "[NOT met]"; }
}
