method IsBreakEvenTest(){
  var res1:=IsBreakEven(1500,1200);
  print(res1);print("\n");
  assert res1==false;

  var res2:=IsBreakEven(100,100);
  print(res2);print("\n");
  assert res2==true;

  var res3:=IsBreakEven(2000,5000);
  print(res3);print("\n");
  assert res3==false;

}