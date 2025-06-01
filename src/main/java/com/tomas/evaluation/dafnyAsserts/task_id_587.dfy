method ArrayToSeqTest(){
  var a1:= new int[] [5, 10, 7, 4, 15, 3];
  var res1:=ArrayToSeq(a1);
  print(res1);print("\n");
              //expected [5, 10, 7, 4, 15, 3]


  var a2:= new int[] [2, 4, 5, 6, 2, 3, 4, 4, 7];
  var res2:=ArrayToSeq(a2);
  print(res2);print("\n");
              //expected [2, 4, 5, 6, 2, 3, 4, 4, 7]


  var a3:= new int[] [58,44,56];
  var res3:=ArrayToSeq(a3);
  print(res3);print("\n");
              //expected [58,44,56]

}