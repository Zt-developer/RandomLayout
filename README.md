# RandomLayout
	这是一个轻量级的随机布局,多彩文字.
	# 使用方法 #
	 StellAdapter adapter = new StellAdapter(lists);
     stellarMap.setAdapter(adapter);
     //设置默认显示第几组,否则刚进来不显示数据
     stellarMap.setGroup(0, true);
     //设置在SteallarMap中显示的View的密度,只要x,y相乘大于每页n个就行，不要搞特别大
     stellarMap.setRegularity(4, 4);
	

