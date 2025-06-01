method DegreesToRadiansTest(){
  var res1:=DegreesToRadians(90.0);
  print(res1);print("\n");
              //assert res1==1.5707963267948966;

  var res2:=DegreesToRadians(60.0);
  print(res2);print("\n");
              //assert res2==1.0471975511965976;

  var res3:=DegreesToRadians(120.0);
  print(res3);print("\n");
              //assert res3==2.0943951023931953;

}