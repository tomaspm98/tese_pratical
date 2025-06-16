method ElementWiseDivisionTest(){
  var s1: seq<int> := [10, 4, 6, 9];
  var s2: seq<int> := [5, 2, 3, 3];
  var res1:=ElementWiseDivision(s1,s2);
  print(res1);print("\n");
              //expected [2, 2, 2, 3];


  var s3: seq<int> := [12, 6, 8, 16];
  var s4: seq<int> := [6, 3, 4, 4];
  var res2:=ElementWiseDivision(s3,s4);
  print(res2);print("\n");
              //expected [2, 2, 2, 4];


  var s5: seq<int> := [20, 14, 36, 18];
  var s6: seq<int> := [5, 7, 6, 9];
  var res3:=ElementWiseDivision(s5,s6);
  print(res3);print("\n");
              //expected [4, 2, 6, 2];

}