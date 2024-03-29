/* Generated By:JavaCC: Do not edit this line. Parser.java */
public class Parser implements ParserConstants {
    public static void main (String args[]){
        Parser parser;

        if (args.length == 0){
            System.out.println("Reading the Input...");
            parser = new Parser(System.in);
        }
        else if (args.length == 1){
            try{
                System.out.println("Reading the Archive Input...");
                parser = new Parser(new java.io.FileInputStream(args[0]));
            }
            catch(java.io.FileNotFoundException e) {
                System.out.println("Archive not found.");
                return;
            }
        }
        else{
            System.out.println("Out of Pattern!");
            return;
        }
        try{
            //parser.Program();
            System.out.println("Succesfull Reading !!!\u005cnShowing Tokens...\u005cn");

            Token token;
            while((token = parser.getNextToken()).kind != 0){
                System.out.println(token.image);
            }
        }
        catch(TokenMgrError e){
            System.out.println("Unsuccessful Reading. Showing Error Message:");
            System.out.println(e.getMessage());
        }
    }

// Expressions Declaration - Syntax Parser

// The Program Method is the where you denote the outer structure of the mini java code
// Basically the program has two parts: The Main Class and Other Classes
// The MainClass() obsviously capture the tokens of the class with the function static main
// and all other classes in the program are captured in OthersClasses()
  static final public void Program() throws ParseException {
    MainClass();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CLASS:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      OthersClasses();
    }
  }

//Method responsible for capturing the MainClass defined in the format:
    // class <class_name> {
    // public static void main ( String[] <name_of_list>)
    // {<CODE>}
    // }
  static final public void MainClass() throws ParseException {
    jj_consume_token(CLASS);
    jj_consume_token(ID);
    jj_consume_token(LBRACE);
    jj_consume_token(PUBLIC);
    jj_consume_token(STATIC);
    jj_consume_token(VOID);
    jj_consume_token(MAIN);
    jj_consume_token(LPAR);
    jj_consume_token(STRING);
    jj_consume_token(LBRACKET);
    jj_consume_token(RBRACKET);
    jj_consume_token(ID);
    jj_consume_token(RPAR);
    jj_consume_token(LBRACE);
    Statement();
    jj_consume_token(RBRACE);
    jj_consume_token(RBRACE);
  }

//This method captures all classes in the code, except for the main, the format is:
    // class <class_name>{
    //  Attributes;
    //  Methods;
    // }
// Or in the format:
    // class <class_name> extends <superclass_name>{
    //  Attributes;
    //  Methods;
    // }
  static final public void OthersClasses() throws ParseException {
    if (jj_2_1(3)) {
      jj_consume_token(CLASS);
      jj_consume_token(ID);
      jj_consume_token(LBRACE);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INT:
        case BOOLEAN:
        case ID:
          ;
          break;
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        AttrDefinition();
      }
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case PUBLIC:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_3;
        }
        MethodDefinition();
      }
      jj_consume_token(RBRACE);
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CLASS:
        jj_consume_token(CLASS);
        jj_consume_token(ID);
        jj_consume_token(EXTENDS);
        jj_consume_token(ID);
        jj_consume_token(LBRACE);
        label_4:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case INT:
          case BOOLEAN:
          case ID:
            ;
            break;
          default:
            jj_la1[3] = jj_gen;
            break label_4;
          }
          AttrDefinition();
        }
        label_5:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case PUBLIC:
            ;
            break;
          default:
            jj_la1[4] = jj_gen;
            break label_5;
          }
          MethodDefinition();
        }
        jj_consume_token(RBRACE);
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

// Attribute Definition, denoted by the format:
//  type(int, int[], bool or object(denoted by <ID>)) <attr_name> ;
  static final public void AttrDefinition() throws ParseException {
    Type();
    jj_consume_token(ID);
    jj_consume_token(SCOLON);
  }

//Method Definition, in format:
//  public type <method_name>(<parameters>){
//      Attributes;
//      <CODE>
//      return <Expression>;
//  }
  static final public void MethodDefinition() throws ParseException {
    jj_consume_token(PUBLIC);
    Type();
    jj_consume_token(ID);
    jj_consume_token(LPAR);
    ParametersList();
    jj_consume_token(RPAR);
    jj_consume_token(LBRACE);
    label_6:
    while (true) {
      if (jj_2_2(2)) {
        ;
      } else {
        break label_6;
      }
      AttrDefinition();
    }
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
      case IF:
      case WHILE:
      case PRINT:
      case ID:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_7;
      }
      Statement();
    }
    jj_consume_token(RETURN);
    Expression();
    jj_consume_token(SCOLON);
    jj_consume_token(RBRACE);
  }

// Function to check type tokens, classified in 4 possibilities:
// array of int, int, boolean and a class name
  static final public void Type() throws ParseException {
    if (jj_2_3(2)) {
      jj_consume_token(INT);
      jj_consume_token(RBRACKET);
      jj_consume_token(LBRACKET);
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
        jj_consume_token(BOOLEAN);
        break;
      case INT:
        jj_consume_token(INT);
        break;
      case ID:
        jj_consume_token(ID);
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

// Function to denote empty
  static final public void Empty() throws ParseException {

  }

// Function Parameters list is used in the input of methods, can be multiples "int something" or just empty
  static final public void ParametersList() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
    case BOOLEAN:
    case ID:
      Type();
      jj_consume_token(ID);
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[8] = jj_gen;
          break label_8;
        }
        OthersParameters();
      }
      break;
    default:
      jj_la1[9] = jj_gen;
      Empty();
    }
  }

// Auxiliar function to create parameter list
  static final public void OthersParameters() throws ParseException {
    jj_consume_token(COMMA);
    Type();
    jj_consume_token(ID);
  }

// Now we have the most importants part of the code:
    // Statements and Expressions
    // Statements are block of codes, if-else, while, prints, attributions, etc;
    // Expression compose Statements where Expression are mathematical/boolean expressions.

// This first function obtain the format of various differents Statements
  static final public void Statement() throws ParseException {
    if (jj_2_4(2)) {
      jj_consume_token(LBRACE);
      label_9:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LBRACE:
        case IF:
        case WHILE:
        case PRINT:
        case ID:
          ;
          break;
        default:
          jj_la1[10] = jj_gen;
          break label_9;
        }
        Statement();
      }
      jj_consume_token(RBRACE);
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IF:
        jj_consume_token(IF);
        jj_consume_token(LPAR);
        Expression();
        jj_consume_token(RPAR);
        Statement();
        jj_consume_token(ELSE);
        Statement();
        break;
      case WHILE:
        jj_consume_token(WHILE);
        jj_consume_token(LPAR);
        Expression();
        jj_consume_token(RPAR);
        Statement();
        break;
      case PRINT:
        jj_consume_token(PRINT);
        jj_consume_token(LPAR);
        Expression();
        jj_consume_token(RPAR);
        jj_consume_token(SCOLON);
        break;
      default:
        jj_la1[11] = jj_gen;
        if (jj_2_5(2)) {
          jj_consume_token(ID);
          jj_consume_token(EQUAL);
          Expression();
          jj_consume_token(SCOLON);
        } else if (jj_2_6(2)) {
          jj_consume_token(ID);
          jj_consume_token(LBRACKET);
          Expression();
          jj_consume_token(RBRACKET);
          jj_consume_token(EQUAL);
          Expression();
          jj_consume_token(SCOLON);
        } else {
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
  }

// To resolve the problem of Left-Recursion we built the structure (Expression -> Expression_final)
// with this format we force Right-Recursion, avoiding the Left-Recursion problem

// Obs: Probably the Expression_final is incomplete, but seems OK to first tests
  static final public void Expression_Final() throws ParseException {
    if (jj_2_7(3)) {
      jj_consume_token(LAND);
      Expression_Final();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SMALLER:
        jj_consume_token(SMALLER);
        Expression_Final();
        break;
      case PLUS:
        jj_consume_token(PLUS);
        Expression_Final();
        break;
      case MINUS:
        jj_consume_token(MINUS);
        Expression_Final();
        break;
      case MULT:
        jj_consume_token(MULT);
        Expression_Final();
        break;
      case LBRACKET:
        jj_consume_token(LBRACKET);
        Expression();
        jj_consume_token(RBRACKET);
        Expression_Final();
        break;
      default:
        jj_la1[12] = jj_gen;
        if (jj_2_8(2)) {
          jj_consume_token(DOT);
          jj_consume_token(LENGTH);
          Expression_Final();
        } else if (jj_2_9(2)) {
          jj_consume_token(DOT);
          jj_consume_token(ID);
          jj_consume_token(LPAR);
          ExpressionList();
          jj_consume_token(RPAR);
        } else {
          Empty();
        }
      }
    }
  }

  static final public void Expression() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
      jj_consume_token(TRUE);
      Expression_Final();
      break;
    case FALSE:
      jj_consume_token(FALSE);
      Expression_Final();
      break;
    case ID:
      jj_consume_token(ID);
      Expression_Final();
      break;
    case NUM:
      jj_consume_token(NUM);
      Expression_Final();
      break;
    default:
      jj_la1[13] = jj_gen;
      if (jj_2_10(2)) {
        jj_consume_token(NEW);
        jj_consume_token(INT);
        jj_consume_token(LBRACKET);
        Expression();
        jj_consume_token(RBRACKET);
        Expression_Final();
      } else if (jj_2_11(2)) {
        jj_consume_token(NEW);
        jj_consume_token(ID);
        jj_consume_token(LPAR);
        jj_consume_token(RPAR);
        Expression_Final();
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LNOT:
          jj_consume_token(LNOT);
          Expression();
          break;
        case LPAR:
          jj_consume_token(LPAR);
          Expression();
          jj_consume_token(RPAR);
          Expression_Final();
          break;
        default:
          jj_la1[14] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
  }

//Auxiliar methods to have a list of Expressions to use as input in methods
  static final public void ExpressionList() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAR:
    case NEW:
    case TRUE:
    case FALSE:
    case LNOT:
    case NUM:
    case ID:
      Expression();
      label_10:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[15] = jj_gen;
          break label_10;
        }
        ExpressionContinue();
      }
      break;
    default:
      jj_la1[16] = jj_gen;
      Empty();
    }
  }

  static final public void ExpressionContinue() throws ParseException {
    jj_consume_token(COMMA);
    Expression();
  }

  static private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  static private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  static private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  static private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  static private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  static private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  static private boolean jj_2_8(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(7, xla); }
  }

  static private boolean jj_2_9(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(8, xla); }
  }

  static private boolean jj_2_10(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_10(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(9, xla); }
  }

  static private boolean jj_2_11(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_11(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(10, xla); }
  }

  static private boolean jj_3R_32() {
    if (jj_scan_token(LPAR)) return true;
    return false;
  }

  static private boolean jj_3R_31() {
    if (jj_scan_token(LNOT)) return true;
    return false;
  }

  static private boolean jj_3_11() {
    if (jj_scan_token(NEW)) return true;
    if (jj_scan_token(ID)) return true;
    return false;
  }

  static private boolean jj_3_10() {
    if (jj_scan_token(NEW)) return true;
    if (jj_scan_token(INT)) return true;
    return false;
  }

  static private boolean jj_3R_30() {
    if (jj_scan_token(NUM)) return true;
    return false;
  }

  static private boolean jj_3R_29() {
    if (jj_scan_token(ID)) return true;
    return false;
  }

  static private boolean jj_3R_28() {
    if (jj_scan_token(FALSE)) return true;
    return false;
  }

  static private boolean jj_3R_27() {
    if (jj_scan_token(TRUE)) return true;
    return false;
  }

  static private boolean jj_3R_25() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_27()) {
    jj_scanpos = xsp;
    if (jj_3R_28()) {
    jj_scanpos = xsp;
    if (jj_3R_29()) {
    jj_scanpos = xsp;
    if (jj_3R_30()) {
    jj_scanpos = xsp;
    if (jj_3_10()) {
    jj_scanpos = xsp;
    if (jj_3_11()) {
    jj_scanpos = xsp;
    if (jj_3R_31()) {
    jj_scanpos = xsp;
    if (jj_3R_32()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  static private boolean jj_3R_11() {
    if (jj_3R_14()) return true;
    if (jj_scan_token(ID)) return true;
    return false;
  }

  static private boolean jj_3R_21() {
    if (jj_3R_26()) return true;
    return false;
  }

  static private boolean jj_3_9() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(ID)) return true;
    return false;
  }

  static private boolean jj_3_8() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(LENGTH)) return true;
    return false;
  }

  static private boolean jj_3R_20() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_3R_25()) return true;
    return false;
  }

  static private boolean jj_3R_19() {
    if (jj_scan_token(MULT)) return true;
    if (jj_3R_13()) return true;
    return false;
  }

  static private boolean jj_3R_18() {
    if (jj_scan_token(MINUS)) return true;
    if (jj_3R_13()) return true;
    return false;
  }

  static private boolean jj_3R_17() {
    if (jj_scan_token(PLUS)) return true;
    if (jj_3R_13()) return true;
    return false;
  }

  static private boolean jj_3_1() {
    if (jj_scan_token(CLASS)) return true;
    if (jj_scan_token(ID)) return true;
    if (jj_scan_token(LBRACE)) return true;
    return false;
  }

  static private boolean jj_3R_16() {
    if (jj_scan_token(SMALLER)) return true;
    if (jj_3R_13()) return true;
    return false;
  }

  static private boolean jj_3R_26() {
    return false;
  }

  static private boolean jj_3_7() {
    if (jj_scan_token(LAND)) return true;
    if (jj_3R_13()) return true;
    return false;
  }

  static private boolean jj_3R_13() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_7()) {
    jj_scanpos = xsp;
    if (jj_3R_16()) {
    jj_scanpos = xsp;
    if (jj_3R_17()) {
    jj_scanpos = xsp;
    if (jj_3R_18()) {
    jj_scanpos = xsp;
    if (jj_3R_19()) {
    jj_scanpos = xsp;
    if (jj_3R_20()) {
    jj_scanpos = xsp;
    if (jj_3_8()) {
    jj_scanpos = xsp;
    if (jj_3_9()) {
    jj_scanpos = xsp;
    if (jj_3R_21()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  static private boolean jj_3_2() {
    if (jj_3R_11()) return true;
    return false;
  }

  static private boolean jj_3R_12() {
    if (jj_3R_15()) return true;
    return false;
  }

  static private boolean jj_3_6() {
    if (jj_scan_token(ID)) return true;
    if (jj_scan_token(LBRACKET)) return true;
    return false;
  }

  static private boolean jj_3_5() {
    if (jj_scan_token(ID)) return true;
    if (jj_scan_token(EQUAL)) return true;
    return false;
  }

  static private boolean jj_3R_24() {
    if (jj_scan_token(PRINT)) return true;
    return false;
  }

  static private boolean jj_3_3() {
    if (jj_scan_token(INT)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  static private boolean jj_3R_14() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_3()) {
    jj_scanpos = xsp;
    if (jj_scan_token(37)) {
    jj_scanpos = xsp;
    if (jj_scan_token(36)) {
    jj_scanpos = xsp;
    if (jj_scan_token(40)) return true;
    }
    }
    }
    return false;
  }

  static private boolean jj_3R_23() {
    if (jj_scan_token(WHILE)) return true;
    return false;
  }

  static private boolean jj_3R_22() {
    if (jj_scan_token(IF)) return true;
    return false;
  }

  static private boolean jj_3_4() {
    if (jj_scan_token(LBRACE)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_12()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  static private boolean jj_3R_15() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_4()) {
    jj_scanpos = xsp;
    if (jj_3R_22()) {
    jj_scanpos = xsp;
    if (jj_3R_23()) {
    jj_scanpos = xsp;
    if (jj_3R_24()) {
    jj_scanpos = xsp;
    if (jj_3_5()) {
    jj_scanpos = xsp;
    if (jj_3_6()) return true;
    }
    }
    }
    }
    }
    return false;
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public ParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[17];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x400000,0x0,0x40000,0x0,0x40000,0x400000,0x25400,0x0,0x10,0x0,0x25400,0x25000,0x38000100,0x6000000,0x40,0x10,0x6008040,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x130,0x0,0x130,0x0,0x0,0x100,0x130,0x0,0x130,0x100,0x0,0x2,0x180,0x1,0x0,0x181,};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[11];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 17; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  static private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        exists = true;
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.add(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[47];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 17; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 47; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

  static private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 11; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
            case 7: jj_3_8(); break;
            case 8: jj_3_9(); break;
            case 9: jj_3_10(); break;
            case 10: jj_3_11(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  static private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
