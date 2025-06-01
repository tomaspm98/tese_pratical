method MonthHas31DaysTest(){
  var res1:=MonthHas31Days(5);
  print(res1);print("\n");
  assert res1==true;

  var res2:=MonthHas31Days(2);
  print(res2);print("\n");
  assert res2==false;

  var res3:=MonthHas31Days(6);
  print(res3);print("\n");
  assert res3==false;

}