method FindFirstRepeatedCharTest(){
  var found1, out1 :=FindFirstRepeatedChar("abcabc");
  expect out1=='a';

  var found2, out2 :=FindFirstRepeatedChar("axbcx");
  expect out2=='x';

  var found3, out3 :=FindFirstRepeatedChar("123123");
  expect out3=='1';

}
