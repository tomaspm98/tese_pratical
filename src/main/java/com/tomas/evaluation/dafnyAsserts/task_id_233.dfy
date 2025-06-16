method CylinderLateralSurfaceAreaTest(){
  var res1:= CylinderLateralSurfaceArea(10.0,5.0);
  print(res1);print("\n");
              //assert res1==314.15926535897932384600;
  var res2:= CylinderLateralSurfaceArea(4.0,5.0);
  print(res2);print("\n");
              //assert res2==125.66370614359172953840;
  var res3:= CylinderLateralSurfaceArea(4.0,10.0);
  print(res3);print("\n");
              //assert res3==251.32741228718345907680;


}