method MaxTest(){
  var res1:=Max(5,10);
  print(res1);print("\n");
  assert res1==10;
  var res2:=Max(-1,-2);
  print(res2);print("\n");
  assert res2==-1;
  var res3:=Max(9,7);
  print(res3);print("\n");
  assert res3==9;
}