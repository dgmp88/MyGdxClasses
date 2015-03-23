package com.boondog.imports.misc;

public interface AdController {
	public void showBottomBannerAd(boolean show);
	public int getBottomBannerHeightPixels();
	
	public void showInterstitialAd(float seconds);
	public void loadNextInterstitialAd();
}
