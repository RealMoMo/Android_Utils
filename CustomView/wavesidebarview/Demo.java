
	private WaveSideBar mSideBar;

    private void initSideBar() {
		//set content view into sidebar
        View content = LayoutInflater.from(this).inflate(R.layout.layout_setting,
                null, false);
     
        mSideBar.setView(content);
      
    }