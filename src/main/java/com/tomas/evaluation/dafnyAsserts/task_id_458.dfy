method RectangleAreaTest(){

  var out1:=RectangleArea(10,20);
  print(out1);print("\n");
  assert out1==200;

  var out2:=RectangleArea(10,5);
  print(out2);print("\n");
  assert out2==50;

  var out3:=RectangleArea(4,2);
  print(out3);print("\n");
  assert out3==8;


}