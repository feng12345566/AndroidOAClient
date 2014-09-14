package com.mct.client;

import com.mct.fragment.DraftBoxFragment;
import com.mct.fragment.OutboxFragment;

import android.os.Bundle;

public class DraftBoxActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		DraftBoxFragment fragment=new DraftBoxFragment();
		Bundle bundle=new Bundle();
		bundle.putString("tag", "draft");
		setParameter("≤›∏Âœ‰", fragment, null, "∑µªÿ", null);
	}

}
