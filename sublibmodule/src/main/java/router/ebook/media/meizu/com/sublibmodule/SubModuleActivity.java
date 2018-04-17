package router.ebook.media.meizu.com.sublibmodule;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.olbb.router.annotations.Router;

@Router(path = "sub_activity")
public class SubModuleActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_act);
    }
}
