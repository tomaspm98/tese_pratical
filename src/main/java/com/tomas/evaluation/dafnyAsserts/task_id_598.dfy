method IsArmstrongTest(){
  var res1:=IsArmstrong(153);
  print(res1);print("\n");
  assert res1==true;

  var res2:=IsArmstrong(259);
  print(res2);print("\n");
  assert res2==false;

  var res3:=IsArmstrong(4458);
  print(res3);print("\n");
  assert res3==false;

}