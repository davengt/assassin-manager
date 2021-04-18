//Daven Giftian Tejalaksana
//Sunday, 18 April 2021
//CSE 143
//Instructor: Stuart Reges
//TA: Andrew Cheng
//Assignment #3
//This program allows the client to manage a game of assassin, tracking assassins and victims.

import java.util.*;

public class AssassinManager {
   private AssassinNode killRing; //Reference to the front of assassins that are alive.
   private AssassinNode graveyard; //Reference to the front of victims (killed assassins).
   
   //Pre: "names" list should not be empty (Throws IllegalArgumentException if not).
      //Names in the list are nonempty strings with no duplicate names (ignoring case).
   //Post: Constructs an assassin manager object which add the names from list into kill ring.
      //Names in kill ring is in the same order in which they appear in the list.
   public AssassinManager(List<String> names) {
      if (names.size() == 0) {
         throw new IllegalArgumentException("List should not be empty.");
      }
      
      killRing = new AssassinNode(names.get(0));
      AssassinNode current = killRing;
      for (int i = 1; i < names.size(); i++) {
         current.next = new AssassinNode(names.get(i));
         current = current.next;
      }
   }
   
   //Post: Prints the name of the people in the kill ring, one per line, indented 4 spaces.
      //Output form: "<name> is stalking <name>"
      //If only one person in the ring, method reports that the person is stalking themselves.
   public void printKillRing() {
      AssassinNode current = killRing.next;
      AssassinNode prev = killRing;
      if (current == null) {
         System.out.println("    " + prev.name + " is stalking " + prev.name);
      } else {
         while (current != null) {
            System.out.println("    " + prev.name + " is stalking " + current.name);
            prev = current;
            current = current.next;
         }
         System.out.println("    " + prev.name + " is stalking " + killRing.name);
      }
   }
   
   //Post: Prints the name of the people in the graveyard (one per line, indented 4 spaces).
      //The output form is "<name> was killed by <name>".
      //Names are printed in reverse kill order:
         //It starts from most recently killed, then next most recently killed, and so on.
      //No output is produced if the graveyard is empty.
   public void printGraveyard() {
      AssassinNode current = graveyard;
      while (current != null) {
         System.out.println("    " + current.name + " was killed by " + current.killer);
         current = current.next;
      }
   }
   
   //Post: Returns true if the given name is in the current kill ring, returns false otherwise.
      //Ignores case when comparing names.
   public boolean killRingContains(String name) {
      AssassinNode current = killRing;
      while (current != null) {
         if (current.name.toLowerCase().equals(name.toLowerCase())) {
            return true;
         }
         current = current.next;
      }
      
      return false;
   }
   
   //Post: returns true if given name is in current graveyard, returns false otherwise.
      //Ignores case when comparing names.
   public boolean graveyardContains(String name) {
      AssassinNode current = graveyard;
      while (current != null) {
         if (current.name.toLowerCase().equals(name.toLowerCase())) {
            return true;
         }
         current = current.next;
      }
      return false;
   }
   
   //Post: returns true if the game is over (when kill ring only has one person in it).
      //Returns false otherwise.
   public boolean gameOver() {
      return killRing.next == null;
   }
   
   //Post: return the name of the winner of the game. Returns null if game is not over.
   public String winner() {
      if (gameOver()) {
         return killRing.name;
      }
      return null;
   }
      
   //Pre: Name should be part of current kill ring (throws IllegalArgumentException if not)
   //Pre: Game should not be over (throws IllegalStateException if not).
   //Post: Records the killing of person with given name.
      //Then, it transfers victim (person killed) from kill ring to graveyard.
      //Method doesn't change kill ring order of printKillRing.
   //Method should ignore case in comparing names.
   public void kill(String name) {
      if (gameOver()) {
         throw new IllegalStateException("Game is over.");
      } else if (!killRingContains(name)) {
         throw new IllegalArgumentException("Name is not part of current kill ring.");
      }
      
      String assassin = ""; //assassin name
      String victim = ""; //victim name
      if (killRing.name.toLowerCase().equals(name.toLowerCase())) { //victim: front of kill ring
         victim = killRing.name;
         killRing = killRing.next;
         AssassinNode current = killRing;
         while (current.next != null) {
            current = current.next;
         }
         assassin = current.name;
      } else { //victim is NOT in the front of kill ring
         AssassinNode prev = killRing;
         AssassinNode current = killRing.next;
         while (current != null) {
            if (current.name.toLowerCase().equals(name.toLowerCase())) {
               victim = current.name;
               assassin = prev.name;
               prev.next = current.next;
            }
            prev = current;
            current = current.next;
         }
      }
      
      if (graveyard == null) { //Graveyard is empty
         graveyard = new AssassinNode(victim);
      } else { //Graveyard is not empty
         AssassinNode currentGraveyard = graveyard;
         graveyard = new AssassinNode(victim, currentGraveyard);
      }
      graveyard.killer = assassin;
   }
}