class Main {
  public static void main(String [ ] args)
  {
    
    /*Test 1: Testing RNG function*//*
    for (int i = 0; i < 100; i++){
      System.out.println(Map.getRandomInt(0, 5));
    }*/
    
    /*Test 2: Room.IsOutside function*//*
    Room[] r = new Room[6];
    r[0] = new Room(0,0,10,10); //'Map'
    r[1] = new Room(-4,0,3,3); //outside the left side
    r[2] = new Room(11,0,3,3); //outside the right side
    r[3] = new Room(7,0,3,3);  //inside the right side
    r[4] = new Room(0,-4,3,3); //outside the top side
    r[5] = new Room(0,8,3,3); //Slightly outside the bottom side
    System.out.println("1. Expecting true: " + r[1].isOutside(r[0]));
    System.out.println("2. Expecting true: " + r[2].isOutside(r[0]));
    System.out.println("3. Expecting false: " + r[3].isOutside(r[0]));
    System.out.println("4. Expecting true: " + r[4].isOutside(r[0]));
    System.out.println("5. Expecting true: " + r[5].isOutside(r[0]));
    */
    
    /*Test 3: Testing Map*/
    
    Map map; //= new Map(33,60);
    for(int i = 0; i < 1; i++){
      map = new Map(100,100);
      map.out();
    }
  }
}