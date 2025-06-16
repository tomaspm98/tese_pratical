method CalculateLossTest(){
  var res1:=CalculateLoss(1500,1200);
  print(res1);print("\n");
              //assert res1==0;

  var res2:=CalculateLoss(100,200);
  print(res2);print("\n");
              //assert res2==100;

  var res3:=CalculateLoss(2000,5000);
  print(res3);print("\n");
              //assert res3==3000;


}