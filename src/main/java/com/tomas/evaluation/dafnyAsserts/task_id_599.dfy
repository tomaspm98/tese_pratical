method SumAndAverageTest(){
  var sum1, avg1 :=SumAndAverage(10);
  assert sum1==55;
  assert avg1==5.5;

  var sum2, avg2 :=SumAndAverage(15);
  assert sum2==120;
  assert avg2==8.0;

  var sum3, avg3 :=SumAndAverage(20);
  assert sum3==210;
  assert avg3==10.5;

}

