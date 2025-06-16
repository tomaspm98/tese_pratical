method IntersectionTest(){
  var a1:= new int[] [1, 2, 3, 5, 7, 8, 9, 10];
  var a2:= new int[] [1, 2, 4, 8, 9];
  var res1:=Intersection(a1,a2);
  print(res1);print("\n");
              // expected [1, 2, 8, 9];

  var a3:= new int[] [1, 2, 3, 5, 7, 8, 9, 10];
  var a4:= new int[] [3,5,7,9];
  var res2:=Intersection(a3,a4);
  print(res2);print("\n");
              // expected [3,5,7,9];

  var a5:= new int[] [1, 2, 3, 5, 7, 8, 9, 10];
  var a6:= new int[] [10,20,30,40];
  var res3:=Intersection(a5,a6);
  print(res3);print("\n");
              // expected [10];

}