method IsOddTest(){
  var res1:=IsOdd(12);
  print(res1);print("\n");
  assert res1==false;
  var res2:=IsOdd(7);
  print(res2);print("\n");
  assert res2==true;
  var res3:=IsOdd(10);
  print(res3);print("\n");
  assert res3==false;

}