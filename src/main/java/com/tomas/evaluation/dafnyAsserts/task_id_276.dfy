method CylinderVolumeTest(){
  var res1:=CylinderVolume(10.0,5.0);
  print(res1);print("\n");
              //assert res1==1570.7500000000002;
  var res2:=CylinderVolume(4.0,5.0);
  print(res2);print("\n");
              //assert res2==251.32000000000002;
  var res3:=CylinderVolume(4.0,10.0);
  print(res3);print("\n");
              //assert res3==502.64000000000004;
}