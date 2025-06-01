method IsMonthWith30DaysTest(){
  var out1:=IsMonthWith30Days(6);
  print(out1);print("\n");
  assert out1==true;

  var out2:=IsMonthWith30Days(2);
  print(out2);print("\n");
  assert out2==false;

  var out3:=IsMonthWith30Days(12);
  print(out3);print("\n");
  assert out3==false;
}