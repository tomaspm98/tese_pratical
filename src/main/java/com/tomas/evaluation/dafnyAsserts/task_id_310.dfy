method ToCharArrayTest(){
  var e1: seq<char> := ['p', 'y', 't', 'h', 'o', 'n',' ','3', '.', '0'];
  var res1:=ToCharArray("python 3.0");
  PrintArray(res1);
  //expected ['p','y','t','h','o','n',' ','3','.','0']

  var e2: seq<char> := ['i', 't', 'e', 'm', '1'];
  var res2:=ToCharArray("item1");
  PrintArray(res2);
  //expected  ['i','t','e','m','1']

  var e3: seq<char> := ['1', '5', '.', '1', '0'];
  var res3:=ToCharArray("15.10");
  PrintArray(res3);
  //expected '1','5','.','1','0']

}