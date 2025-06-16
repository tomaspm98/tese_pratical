method DifferenceTest(){
  var a1:seq<int>:=[10, 15, 20, 25, 30, 35, 40];
  var a2:seq<int>:= [25, 40, 35];
  var res1:=Difference(a1,a2);
  print(res1);print("\n");
              //expected [10, 15, 20, 30]

  var a3:seq<int>:= [1,2,3,4,5];
  var a4:seq<int>:= [6,7,1];
  var res2:=Difference(a3,a4);
  print(res2);print("\n");
              //expected [2, 3, 4, 5]

  var a5:seq<int>:= [1,2,3];
  var a6:seq<int>:= [6,7,1];
  var res3:=Difference(a5,a6);
  print(res3);print("\n");
              //expected [2, 3]
}