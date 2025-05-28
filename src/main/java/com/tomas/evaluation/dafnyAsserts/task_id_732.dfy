method ReplaceWithColonTest(){

  var out1:=ReplaceWithColon("Python language, Programming language.");
  expect out1=="Python:language::Programming:language:";

  var out2:=ReplaceWithColon("a b c,d e f");
  expect out2=="a:b:c:d:e:f";

  var out3:=ReplaceWithColon("ram reshma,ram rahim");
  expect out3=="ram:reshma:ram:rahim";

}

