method NthNonagonalNumberTest(){
  var res1:= NthNonagonalNumber(10);
  print(res1);print("\n");
  assert res1==325;

  var res2:= NthNonagonalNumber(15);
  print(res2);print("\n");
  assert res2==750;

  var res3:= NthNonagonalNumber(18);
  print(res3);print("\n");
  assert res3==1089;

}