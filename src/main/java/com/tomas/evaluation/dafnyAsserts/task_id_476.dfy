method SumMinMaxTest(){
  var a1:= new int[] [1,2,3];
  var out1:=SumMinMax(a1);
  print(out1);print("\n");
              //assert out1==4;

  var a2:= new int[] [-1,2,3,4];
  var out2:=SumMinMax(a2);
  print(out2);print("\n");
              //assert out2==3;

  var a3:= new int[] [2,3,6];
  var out3:=SumMinMax(a3);
  print(out3);print("\n");
              //assert out3==8;
}