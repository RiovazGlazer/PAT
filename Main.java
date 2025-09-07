/**
 * Thabiso Motimele elective 1 Library Book Management System (LBMS)
 * 
 * Features:
 * - Scanner Class with switch/if statements  in CLI (shows programming constructs)
 * - Tabbed Swing GUI (default startup)
 * - Arrays of objects for storage (Book[], Borrower[], Transaction[])
 * - Text file  used to save and load data to .txt. 
 * - LocalDate for borrow/return/due handling dates
 * - Defensive programming and comments throughout
 * Use "book.txt", "user.txt" and "tx.txt" file to see BOOK ID AND USER ID in file
 * Comments explain all methods
 */

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

//  Main Class 
public class Main {
    public static void main(String[] args){
        FileHandler fh=new FileHandler("data");
        Library lib=new Library(fh); lib.load();
        
        new TabbedSwing(lib);
        
        lib.save();
    }


    }

// ----- Domain Classes -----

/** Represents a book in the library. */
class Book {
    String id, title, author, category, isbn;
    int year;
    boolean available;

    Book(String id, String title, String author, String category, String isbn, int year, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
        this.year = year;
        this.available = available;
    }

    String toCSV() { return id + "," + title + "," + author + "," + category + "," + isbn + "," + year + "," + available; }

    static Book fromCSV(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 7) return null;
        return new Book(p[0], p[1], p[2], p[3], p[4], Integer.parseInt(p[5]), Boolean.parseBoolean(p[6]));
    }
}

/** Represents a borrower/user. */
class Borrower {
    String id, name, phone, email;

    Borrower(String id, String name, String phone, String email) {
        this.id = id; this.name = name; this.phone = phone; this.email = email;
    }

    String toCSV() { return id + "," + name + "," + phone + "," + email; }

    static Borrower fromCSV(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 4) return null;
        return new Borrower(p[0], p[1], p[2], p[3]);
    }
}

/** Represents a borrow/return transaction. */
class Transaction {
    String id, bookId, borrowerId;
    LocalDate borrowDate, dueDate, returnDate;


    Transaction(String id, String bookId, String borrowerId, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id; this.bookId = bookId; this.borrowerId = borrowerId;
        this.borrowDate = borrowDate; this.dueDate = dueDate; this.returnDate = returnDate;
    }

    String toCSV() {
        return id + "," + bookId + "," + borrowerId + "," +
               (borrowDate==null?"":borrowDate) + "," +
               (dueDate==null?"":dueDate) + "," +
               (returnDate==null?"":returnDate);
    }

    static Transaction fromCSV(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 6) return null;
        LocalDate b = p[3].isEmpty() ? null : LocalDate.parse(p[3]);
        LocalDate d = p[4].isEmpty() ? null : LocalDate.parse(p[4]);
        LocalDate r = p[5].isEmpty() ? null : LocalDate.parse(p[5]);
        return new Transaction(p[0], p[1], p[2], b, d, r);
    }
}

// ---- File Handling -----

/** Handles text file persistence for books, users, and transactions. */
class FileHandler {
    public void demoReadBooksFile(){
        try{
            Scanner sc=new Scanner(new File("data/books.txt"));
            while(sc.hasNextLine()){
                String line=sc.nextLine();
                System.out.println("Read: "+line);
                String[] parts=line.split(",");
                if(parts.length>=3) System.out.println("Title = "+parts[1]+", Author = "+parts[2]);
            }
            sc.close();
        }catch(Exception e){
            System.out.println("Error reading books.txt: "+e.getMessage());
        }
 }
    File booksFile, usersFile, txFile;

    FileHandler(String dir) {
        booksFile = new File(dir, "books.txt");
        usersFile = new File(dir, "users.txt");
        txFile = new File(dir, "tx.txt");
        try {
            booksFile.getParentFile().mkdirs();
            booksFile.createNewFile(); usersFile.createNewFile(); txFile.createNewFile();
        } catch (Exception e) { }
    }

    String[] readAll(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        java.util.ArrayList<String> lines = new java.util.ArrayList<String>();
        String s; while ((s = br.readLine()) != null) { if (!s.trim().isEmpty()) lines.add(s); }
        br.close(); return lines.toArray(new String[0]);
    }

    void writeAll(File f, String[] lines) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(f, false));
        for (String s : lines) pw.println(s);
        pw.close();
        
        

    }
}

//  Core Library Logic 

/** Manages arrays of objects and main library logic. */
class Library {
    Book[] books = new Book[1000]; int bookCount = 0;
    Borrower[] users = new Borrower[500]; int userCount = 0;
    Transaction[] txs = new Transaction[5000]; int txCount = 0;

    int nextBook = 1, nextUser = 1, nextTx = 1;
    FileHandler fh; static final int LOAN_DAYS = 14;

    Library(FileHandler fh) { this.fh = fh; }

    /** Loads data from files into arrays. */
    void load() {
        try {
            for (String l : fh.readAll(fh.booksFile)) { Book b = Book.fromCSV(l); if (b!=null) { books[bookCount++] = b; nextBook++; }}
            for (String l : fh.readAll(fh.usersFile)) { Borrower u = Borrower.fromCSV(l); if (u!=null) { users[userCount++] = u; nextUser++; }}
            for (String l : fh.readAll(fh.txFile)) { Transaction t = Transaction.fromCSV(l); if (t!=null) { txs[txCount++] = t; nextTx++; }}
        } catch (Exception e) { }
    }

    /** Saves arrays to text files. */
    void save() {
        try {
            String[] b = new String[bookCount]; for (int i=0;i<bookCount;i++) b[i]=books[i].toCSV();
            fh.writeAll(fh.booksFile,b);
            String[] u = new String[userCount]; for (int i=0;i<userCount;i++) u[i]=users[i].toCSV();
            fh.writeAll(fh.usersFile,u);
            String[] t = new String[txCount]; for (int i=0;i<txCount;i++) t[i]=txs[i].toCSV();
            fh.writeAll(fh.txFile,t);
        } catch (Exception e) { }
    }

    /** Adds a new book (with validation). */
    Book addBook(String title, String author, String cat, String isbn, int year) {
        if (year > LocalDate.now().getYear()) throw new IllegalArgumentException("Year cannot be in future");
        for (int i=0;i<bookCount;i++) if (books[i].isbn.equals(isbn)) throw new IllegalArgumentException("Duplicate ISBN");
        Book b = new Book("B"+nextBook++, title, author, cat, isbn, year, true);
        books[bookCount++] = b; return b;
    }

    boolean editBook(String id, String t, String a, String c, int y) {
        for (int i=0;i<bookCount;i++) if (books[i].id.equals(id)) { books[i].title=t;books[i].author=a;books[i].category=c;books[i].year=y; return true; }
        return false;
    }

    boolean deleteBook(String id) {
        for (int i=0;i<bookCount;i++) if (books[i].id.equals(id)) {
            if (!books[i].available) throw new IllegalStateException("On loan");
            for (int j=i;j<bookCount-1;j++) books[j]=books[j+1]; bookCount--; return true;
        } return false;
    }

    Borrower addUser(String n) { Borrower u=new Borrower("U"+nextUser++, n, "", ""); users[userCount++]=u; return u; }

    Transaction borrow(String bid, String uid) {
        Book b = findBook(bid); if (b==null||!b.available) throw new IllegalStateException("Not avail");
        Borrower u = findUser(uid); if (u==null) throw new IllegalArgumentException("No user");
        Transaction t=new Transaction("T"+nextTx++,bid,uid,LocalDate.now(),LocalDate.now().plusDays(LOAN_DAYS),null);
        txs[txCount++]=t; b.available=false; return t;
    }

    Transaction returnBook(String bid) {
        for (int i=txCount-1;i>=0;i--) {
            Transaction t=txs[i];
            if (t.bookId.equals(bid)&&t.returnDate==null) { t.returnDate=LocalDate.now(); Book b=findBook(bid); if (b!=null) b.available=true; return t; }
        } throw new IllegalArgumentException("No active borrow");
    }

    Book findBook(String id){for(int i=0;i<bookCount;i++) if(books[i].id.equals(id)) return books[i]; return null;}
    Borrower findUser(String id){for(int i=0;i<userCount;i++) if(users[i].id.equals(id)) return users[i]; return null;}
    Book[] allBooks(){Book[] a=new Book[bookCount]; for(int i=0;i<bookCount;i++) a[i]=books[i]; return a;}
    Transaction[] overdue(){java.util.ArrayList<Transaction> list=new java.util.ArrayList<Transaction>(); for(int i=0;i<txCount;i++){Transaction t=txs[i]; if(t.returnDate==null&&t.dueDate.isBefore(LocalDate.now())) list.add(t);} return list.toArray(new Transaction[0]);}
}

//  CLI LOGIC

/** Simple Scanner CLI using switch/if for marking. */
class CLI {
    Library lib; Scanner sc=new Scanner(System.in);
    CLI(Library l){lib=l;}
    void loop(){
        while(true){
            System.out.println("1.Add Book 2.List Books 3.Add User 4.Borrow 5.Return 6.Exit 7.Read Book Storage Files");
            String c=sc.nextLine();
            switch(c){
                case "1":
                    System.out.print("Title:");String t=sc.nextLine();
                    try{Book b=lib.addBook(t,"Auth","Cat","ISBN"+System.nanoTime(),2020);System.out.println("Added "+b.id);}catch(Exception e){System.out.println(e.getMessage());}
                    break;
                case "2":
                    for(Book b:lib.allBooks())System.out.println(b.id+" "+b.title+" "+(b.available?"Avail":"Out"));
                    break;
                case "3":
                    System.out.print("Name:");Borrower u=lib.addUser(sc.nextLine());System.out.println("Added "+u.id);
                    break;
                case "4":
                    System.out.print("Book ID:");String bid=sc.nextLine();
                    System.out.print("User ID:");String uid=sc.nextLine();
                    try{Transaction tr=lib.borrow(bid,uid);System.out.println("Due "+tr.dueDate);}catch(Exception e){System.out.println(e.getMessage());}
                    break;
                case "5":
                    System.out.print("Book ID:");try{lib.returnBook(sc.nextLine());System.out.println("Returned");}catch(Exception e){System.out.println(e.getMessage());}
                    break;
                case "6":
                    return;
                case "7":
                    lib.fh.demoReadBooksFile();
                    break;
                default:
                    System.out.println("Invalid");
            
                
            }
        }
    }
}

//  Tabbed Swing GUI 

/** Full tabbed Swing GUI (default startup). */
class TabbedSwing extends JFrame {
    Library lib; DefaultTableModel bookModel=new DefaultTableModel(new Object[]{"ID","Title","Author","Cat","Year","Avail"},0);

    TabbedSwing(Library l){
        super("Library Book Management System — Swing");
        lib=l; setSize(700,500); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabs=new JTabbedPane();
        tabs.add("Add Book",addPanel()); tabs.add("Manage Books",listPanel()); tabs.add("Borrow Book",borrowPanel());
        tabs.add("Return Book",returnPanel()); tabs.add("Overdue Report",overduePanel());
        add(tabs); refresh(); setVisible(true);
    }

    JPanel addPanel(){
        JPanel p=new JPanel(new GridLayout(6,2));
        JTextField t=new JTextField(),a=new JTextField(),c=new JTextField(),i=new JTextField(),y=new JTextField();
        JButton b=new JButton("Add Book");
        p.add(new JLabel("Title:"));p.add(t);
        p.add(new JLabel("Author:"));p.add(a);
        p.add(new JLabel("Category:"));p.add(c);
        p.add(new JLabel("ISBN:"));p.add(i);
        p.add(new JLabel("Year:"));p.add(y);
        p.add(new JLabel(""));p.add(b);
        b.addActionListener(e->{try{lib.addBook(t.getText(),a.getText(),c.getText(),i.getText(),Integer.parseInt(y.getText()));refresh();JOptionPane.showMessageDialog(this,"Added");}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}});
        return p;
    }

    JPanel listPanel(){
        JPanel p=new JPanel(new BorderLayout());
        JTable tbl=new JTable(bookModel); p.add(new JScrollPane(tbl),BorderLayout.CENTER);
        JButton del=new JButton("Delete Book"); p.add(del,BorderLayout.SOUTH);
        del.addActionListener(e->{int r=tbl.getSelectedRow();if(r>=0){String id=(String)tbl.getValueAt(r,0);try{lib.deleteBook(id);}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}refresh();}});
        return p;
    }

    JPanel borrowPanel(){
        JPanel p=new JPanel(); JTextField b=new JTextField(6),u=new JTextField(6); JButton bb=new JButton("Borrow");
        p.add(new JLabel("Book ID"));p.add(b); p.add(new JLabel("User ID"));p.add(u); p.add(bb);
        bb.addActionListener(e->{try{Transaction t=lib.borrow(b.getText(),u.getText());JOptionPane.showMessageDialog(this,"Due "+t.dueDate);}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}});
        return p;
    }

    JPanel returnPanel(){
        JPanel p=new JPanel(); JTextField b=new JTextField(6); JButton r=new JButton("Return");
        p.add(new JLabel("Book ID"));p.add(b); p.add(r);
        r.addActionListener(e->{try{lib.returnBook(b.getText());JOptionPane.showMessageDialog(this,"Returned");}catch(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage());}});
        return p;
    }

    JPanel overduePanel(){
        JPanel p=new JPanel(new BorderLayout());
        DefaultTableModel m=new DefaultTableModel(new Object[]{"Tx","Book","User","Due","DaysLate"},0);
        JTable tbl=new JTable(m); p.add(new JScrollPane(tbl),BorderLayout.CENTER);
        JButton ref=new JButton("Refresh"); p.add(ref,BorderLayout.SOUTH);
        ref.addActionListener(e->{m.setRowCount(0);for(Transaction t:lib.overdue()){long d=ChronoUnit.DAYS.between(t.dueDate,LocalDate.now());m.addRow(new Object[]{t.id,t.bookId,t.borrowerId,t.dueDate,d});}});
        return p;
    }

    void refresh(){bookModel.setRowCount(0); for(Book b:lib.allBooks()) bookModel.addRow(new Object[]{b.id,b.title,b.author,b.category,b.year,b.available});}
}


