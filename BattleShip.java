/**
* File Name: battleShip.java
*
*This run the game battle ship
*
* @author: Honghui
*/


import java.util.Scanner;

public class BattleShip
{
  public static void main(String[] args)
  {
    Scanner input = new Scanner(System.in);
    Player p1;
    Player p2;
    String name;
    boolean noWinner = true;
    String winner = "";
    int round = 1;
    Map smile = new Map();
    Map upset = new Map();
    smile.face(0);
    upset.face(1);

    //read in player names
    System.out.print("Enter player1's name: ");
    name = input.nextLine();
    p1 = new Player(name);
    System.out.print("Enter player2's name: ");
    name = input.nextLine();
    p2 = new Player(name);


    //readIn
    p1.readIn();
    p2.readIn();

    //game
    while(noWinner)
    {
      System.out.println("Round "+round);

      //This seams to be odd but while p1's term p2's variable been modify
      //So I made p2 call the method instead of pass all p2's variable
      p2.otherPlayersTurn(p1);
      p1.otherPlayersTurn(p2);

      if(p1.noShipLeft() && p2.noShipLeft())
      {
        System.out.println("Tie");
        noWinner = false;
        win(round);
        Map.printBothMap(smile,smile);
      }
      else if(p2.noShipLeft())
      {
        noWinner = false;
        win(p2,round);
        Map.printBothMap(smile,upset);
      }
      else if(p1.noShipLeft())
      {
        noWinner = false;
        Map.printBothMap(p1.getShownMap(),p2.getShownMap());
        win(p1,round);
        Map.printBothMap(upset,smile);
      }
      round++;
    }

    System.out.println("Hope you have fun ^_^");
  }

  public static void win(Player player, int round)
  {
    System.out.println("Congratulation "+player.getName()+" you win the game!");
    System.out.println("You win this game in "+round+" rounds!");
    process();
  }

  public static void win(int round)
  {
    System.out.println("Both you won!");
    System.out.println("You finish this game in "+round+" rounds!");
    process();
  }

  public static void process()
  {
    Scanner input = new Scanner(System.in);
    input.nextLine();
  }
}

class Player
{
  private String name;
  private Map map;
  private Map mapShown;
  private Map mapCheck;
  int[][] ships;
  int[] shipCountDown = {5,4,3,3,2};

  public Player(String name)
  {
    this.name = name;
    map = new Map();
    mapShown = new Map();
    mapCheck = new Map();
    ships = new int[5][4];
    //shipCountDown = {5,4,3,3,2};
  }

  public void readIn()
  {
    Scanner input = new Scanner(System.in);
    String yesOrNo, letter;
    String letterList = "ABCDEFGHIJ";
    int number;
    int shipIndex = 0;
    int x=0;
    int y=0;
    String deriection = "";
    int length = 0;
    boolean available = false;
    boolean rightInput = false;
    Map transitionMap = new Map();

    System.out.println();

    // 5 ship total
    for(int i=5;i>=1;i--)
    {
      //get the length of current ship
      //5-5 4-4 3-3 2-3 1-1
      if(i>=3)
        length = i;
      else
        length = i+1;

        //check if the place is available
        while(!available)
        {
          while(!rightInput)
          {
            transitionMap.printMap();
            System.out.println(name+", where do you want to place your #"+ (shipIndex+1) +" ship? (length = "+length+")");
            System.out.print("Enter a letter and number for ship's start point (ex: A 0): ");
            letter = input.next();
            number = input.nextInt();

            if(letterList.indexOf(letter)!=-1 && number >=0 && number <=9)
            {
              //transfer to (x,y)
              x = letterList.indexOf(letter)+1;
              y = number+1;
              rightInput = true;
            }
            else
              System.out.println("Unavailable input, try again ^_^");
          }

          //reset rightInput to false for next input check
          rightInput = false;

          //cheak if input is available
          while(!rightInput)
          {
            System.out.print("Enter R or D for deriection(right/down): ");
            deriection = input.next();
            if(deriection.equals("R") || deriection.equals("D"))
              rightInput = true;
            else
              System.out.println("Only R or D accepted @_@");
          }
          //reset
          rightInput = false;

          available = transitionMap.checkAvailable(length,x,y,deriection);

          if(available)
          {
            transitionMap.markMap(length,x,y,deriection);
            transitionMap.printMap();
            System.out.print("Is this where you want to put #"+(shipIndex+1)+ " your ship? (Y/N) : ");
            yesOrNo = input.next();

            if(yesOrNo.equals("Y"))
            {
              ships[shipIndex][0] = x;
              ships[shipIndex][1] = y;
              //use a number to keep deriection
              if(deriection.equals("R"))
                ships[shipIndex][2] = 0;//0 for right, keep in the same array
              else
                ships[shipIndex][2] = 1;//1 for left

              ships[shipIndex][3] = length;
              mapCheck.markCheckMap(shipIndex,x,y,deriection);
              map.copyMap(transitionMap);
              shipIndex++;
            }
            else
            {
              System.out.println("Undo it for you ^_^");
              transitionMap.copyMap(map);
              available = false;//this will continue this while loop
            }
          }
          else
            System.out.println("Unavailable point, try another place.");

        }
        //shipIndex++;
        //reset available
        available = false;
    }

    for(int i=0;i<50;i++)
      System.out.println("no cheat! ^_^");
  }

  public void otherPlayersTurn(Player op)//op for other player
  {
    Scanner input = new Scanner(System.in);
    String deriection = "";
    String letterList = "ABCDEFGHIJ";
    String yesOrNo, letter;
    int number;
    int x=0;
    int y=0;
    int length = 0;
    boolean available = false;
    boolean rightInput = false;
    Map transitionMap = new Map();


    transitionMap.copyMap(mapShown);
    System.out.println("-"+op.getName()+"'s term-");
    process();

    while(!rightInput)
    {
      transitionMap.copyMap(mapShown);

      //make sure input is available
      while(!rightInput)
      {
        System.out.println("Where will "+name+ " put his/her ships? Enter a point(Ex: A 0)");
        letter = input.next();
        number = input.nextInt();

        if(letterList.indexOf(letter)!=-1 && number >=0 && number <=9)
        {
          x = letterList.indexOf(letter)+1;
          y = number+1;
          rightInput = true;
        }
        else
          System.out.println("Unavailable input, try again ^_^");
      }
      //reset
      rightInput = false;

      //check that point, is it already chosen
      available = mapCheck.checkPoint(x,y);

      //ask player  want to undo or not
      if(available)
      {
        transitionMap.markMap(x,y);
        transitionMap.printMap();
        System.out.print(name +" Is it where you want to hit? (Y/N): ");
        yesOrNo = input.next();

        if(yesOrNo.equals("Y"))
          mapShown.copyMap(transitionMap);//?????????????????????????????????????
        else
        {
          System.out.println("Ok, you can chose another place! ^_^");
          process();
          transitionMap.copyMap(mapShown);
          available = false;
        }
       }
       else
         System.out.println("Unavailable point, try another place.");

      //check result
      if(available)
      {
       if(checkResult(x,y))//check it do hit or not
       {
         if(checkSink(mapCheck.getMap()[x][y]))//check if any ship been sank
         {
           showSinkShip(mapCheck.getMap()[x][y]);//show the sink ship on map
           mapShown.printMap();
           System.out.println("you hit it! ^_^");
           System.out.println("And you sank a Ship!!");
           if(noShipLeft())//check if there is no ship left which means someone wins
           {
            System.out.println("You are winning, last chance for your opponent!");
            rightInput = true;// this will get out of current term(the big while loop)
          }
         }
         else
         {
           mapShown.showHit(x,y);
           mapShown.printMap();
           System.out.println("you hit it! ^_^");
        }

        System.out.println("Take an extra hit");
        process();
       }
        else
       {
         mapShown.markX(x,y);// mark X in place that been chosen and didn't hit
         System.out.println("Not here! Now to "+op.getName()+"'s term");
         process();
         rightInput = true;// this will get out of this term(this while loop)
       }
      }
     }

  }

  public boolean checkResult(int x, int y)
  {
    int shipIndex;
    String list ="ABCDE";
    if(!mapCheck.getMap()[x][y].equals(" "))
    {
      shipIndex = list.indexOf(mapCheck.getMap()[x][y]);
      mapCheck.getMap()[x][y] = mapCheck.getMap()[x][y].toLowerCase();
      shipCountDown(shipIndex);
      return true;
    }
    else
      return false;
  }

  public boolean checkSink(String letter)
  {
    String list = "abcde";
    int shipIndex = list.indexOf(letter);

    if(shipCountDown[shipIndex] == 0)
      return true;
    else
      return false;
  }

  public void showSinkShip(String letter)
  {
    String list = "abcde";
    int shipIndex = list.indexOf(letter);
    int x = ships[shipIndex][0];
    int y = ships[shipIndex][1];

    if(ships[shipIndex][2] == 0)
    {
      for(int i = 0;i<ships[shipIndex][3];i++)
        mapShown.getMap()[x][y+i]= map.getMap()[x][y+i];
    }
    else
    {
      for(int i = 0;i<ships[shipIndex][3];i++)
        mapShown.getMap()[x+i][y]= map.getMap()[x+i][y];
    }
  }

  public void showSinkShip(int shipIndex)
  {
    int x = ships[shipIndex][0];
    int y = ships[shipIndex][1];

    if(ships[shipIndex][2] == 0)
    {
      for(int i = 0;i<ships[shipIndex][3];i++)
        mapShown.getMap()[x][y+i]= map.getMap()[x][y+i];
    }
    else
    {
      for(int i = 0;i<ships[shipIndex][3];i++)
        mapShown.getMap()[x+i][y]= map.getMap()[x+i][y];
    }
  }

  public boolean noShipLeft()
  {
    for(int i=0; i<5; i++)//5 ships total
    {
      if(shipCountDown[i] != 0)//if any ship do not be sink return false
        return false;
    }

    return true;
  }

  public void shipCountDown(int i)
  {
    shipCountDown[i]--;

    if(shipCountDown[i] == 0)
    {
      showSinkShip(i);
      System.out.println("You just sink a Ship");
      process();
    }
  }

  public Map getShownMap()
  { return mapShown;}

  public Map getCheckMap()
  { return mapCheck;}

  public String getName()
  { return name;}

  public void process()
  {
    System.out.print("(-Hit enter to continue-) ^_^");
    Scanner input = new Scanner(System.in);
    input.nextLine();
  }

}

class Map
{
  public String[][] map;//make map public so other class can moiy it

  public Map()
  {
    map = new String[12][12];

    //first line" _________ "
    map[0][0] = " ";
    for(int j=1;j<11;j++)
      map[0][j] = "_";
    map[0][11] = " ";

    //middel part"|       |"
    for(int i=1;i<11;i++)
    {
      //first column "|"
      map[i][0] = "|";

      //middle of row "    "
      for(int j=1;j<11;j++)
        map[i][j] = " ";

      //last column "|"
      map[i][11]="|";
    }

    //last line" _________ "
    map[11][0] = "|";
    for(int j=1;j<11;j++)
      map[11][j] = "_";
    map[11][11] = "|";


  }

  public String[][] getMap()
  { return map;}

  public void copyMap(Map beenCopyMap)
  {
    String[][] beenCopy = beenCopyMap.getMap();
    for(int i=0;i<beenCopy.length;i++)
      for(int j=0;j<beenCopy[i].length;j++)
        map[i][j] = beenCopy[i][j];
  }

  public void markMap(int length, int x, int y, String deriection)
  {
      String[][] R = {{"<",">"},{"<","=",">"},{"<","=","=",">"},{"<","=","=","=",">"}};
      String[][] D = {{"^","v"},{"^","#","v"},{"^","#","#","v"},{"^","#","#","#","v"}};
      String[] ship;
      if(deriection.equals("R"))
        ship = R[length-2];
      else
        ship = D[length-2];

    if(deriection.equals("R"))
    {
      for(int i = 0;i<length;i++)
        map[x][y+i]= ship[i];
    }
    else
    {
      for(int i = 0;i<length;i++)
        map[x+i][y]= ship[i];
    }
  }

  public void printMap()
  {
    String[] letters = {" ","A","B","C","D","E","F","G","H","I","J"," "," "};

    for(int i = 0;i<map.length;i++)
    {
      //print a letter every row
      System.out.print(letters[i]);

      for(int j = 0;j<map[0].length;j++)
      {
        System.out.print(map[i][j]);
      }
      System.out.println();
    }
    System.out.println("  0123456789 ");
  }

  public static void printBothMap(Map m1,Map m2)
  {
    String[][] map1 = m1.getMap();
    String[][] map2 = m2.getMap();
    String[] letters = {" ","A","B","C","D","E","F","G","H","I","J"," "};

    for(int i = 0;i<map1.length;i++) //row
    {
      //print a letter every row
      System.out.print(letters[i]);

      for(int j = 0;j<map1[0].length;j++)//column
      {
        System.out.print(map1[i][j]);
      }

      System.out.print("\t");
      System.out.print(letters[i]);

      for(int j = 0;j<map2[0].length;j++)//column
      {
        System.out.print(map2[i][j]);
      }
      System.out.println();
    }
    System.out.println("  0123456789 \t  0123456789 ");
  }

  public void markCheckMap(int shipIndex, int x, int y, String deriection)
  {
    String[] letters = {"A","B","C","D","E"};
    int[] lengths = {5,4,3,3,2};
    int length = lengths[shipIndex];

    if(deriection.equals("R"))
    {
      for(int i = 0;i<length;i++)
        map[x][y+i] = letters[shipIndex];
    }
    else
    {
      for(int i = 0;i<length;i++)
      map[x+i][y]= letters[shipIndex];
    }
  }

  public boolean checkAvailable(int length, int x, int y, String deriection)
  {
    //goes right
    if(deriection.equals("R"))
    {
      for(int i = 0;i<length;i++)
        if(!map[x][y+i].equals(" "))
          return false;
    }
    //gose down
    else
    {
      for(int i = 0;i<length;i++)
        if(!map[x+i][y].equals(" "))
          return false;
    }

    return true;
  }

  public boolean checkPoint(int x,int y)
  {
    String list = "ABCDE ";// if at that point is ABCDE and space
    if(list.indexOf(map[x][y]) == -1)
      return false;
    else
      return true;
  }

  public void markMap(int x,int y)
  {
    map[x][y] = "?";
  }

  public void markX(int x,int y)
  {
    map[x][y] = "X";
  }

  public void showHit(int x, int y)
  {
    map[x][y]="O";
  }

  public void face(int num)
  {
    for(int i=3;i<=8;i++)
      map[8][i]="_";

    if(num == 0)
    {
      //smile
      map[3][2] = "/";
      map[3][3] = "\\";
      map[3][8] = "/";
      map[3][9] = "\\";
      map[4][1] = "/";
      map[4][4] = "\\";
      map[4][7] = "/";
      map[4][10] = "\\";
    }
    else
    {
      map[3][1] = "\\";
      map[3][4] = "/";
      map[3][7] = "\\";
      map[3][10] = "/";
      map[4][2] = "\\";
      map[4][3] = "/";
      map[4][8] = "\\";
      map[4][9] = "/";
    }
  }

}