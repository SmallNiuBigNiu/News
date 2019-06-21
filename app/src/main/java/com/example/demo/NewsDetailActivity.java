package com.example.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebView;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class NewsDetailActivity extends AppCompatActivity {
    private WebView news_webview;
    private EditText edt_comment;
    private Button btn_send;
    private ImageView iv_like;
    private String newsid;
    private String newsjson;
    private boolean isLike = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        news_webview = findViewById(R.id.news_webview);

        edt_comment = findViewById(R.id.edt_comment);
        btn_send = findViewById(R.id.btn_send);
        iv_like = findViewById(R.id.iv_like);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断当前用户是否已登录  没登录先去登陆
                if(BmobUser.getCurrentUser(BmobUser.class) == null){
                    Intent intent = new Intent(NewsDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                final String userid = BmobUser.getCurrentUser(BmobUser.class).getObjectId();

                String content = edt_comment.getText().toString().trim();

                if(content.length() == 0){
                    Toast.makeText(NewsDetailActivity.this, "请输入评论内容",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                BmobDate time = new BmobDate(new Date());

                Comment comment = new Comment();
                comment.setContent(content);
                comment.setNewsid(newsid);
                comment.setUserid(userid);
                comment.setNewsjson(newsjson);
                comment.setTime(time);
                comment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            Toast.makeText(NewsDetailActivity.this, "评论成功",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断当前用户是否已登录  没登录先去登陆
                if(BmobUser.getCurrentUser(BmobUser.class) == null){
                    Intent intent = new Intent(NewsDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                final String userid = BmobUser.getCurrentUser(BmobUser.class).getObjectId();

                //查询是否有该新闻的收藏数据 有的话就更新 没有就添加
                BmobQuery<Like> likeBmobQuery = new BmobQuery<>();
                likeBmobQuery.addWhereEqualTo("userid", userid);
                likeBmobQuery.addWhereEqualTo("newsid", newsid);
                likeBmobQuery.findObjects(new FindListener<Like>() {
                    @Override
                    public void done(List<Like> list, BmobException e) {
                        if(e == null){
                            // 说明已经有了针对该新闻的收藏收据
                            if(list != null && list.size() > 0){
                                Like like = list.get(0);
                                like.setLike(!isLike);
                                like.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e == null){
                                            isLike = !isLike;
                                            sendBroadcast(new Intent().setAction("LIKE_CHANGED"));
                                            if(isLike){
                                                iv_like.setImageResource(R.drawable.redstar);
                                            }else{
                                                iv_like.setImageResource(R.drawable.blackstar);
                                            }
                                        }
                                    }
                                });
                            }else{
                                // 如果没有 新建数据 保存
                                Like like = new Like();
                                like.setUserid(userid);
                                like.setNewsid(newsid);
                                like.setNewsjson(newsjson);
                                like.setLike(true);
                                like.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e == null){
                                            isLike = !isLike;
                                            sendBroadcast(new Intent().setAction("LIKE_CHANGED"));
                                            if(isLike){
                                                iv_like.setImageResource(R.drawable.redstar);
                                            }else{
                                                iv_like.setImageResource(R.drawable.blackstar);
                                            }
                                        }
                                    }
                                });

                            }
                        }
                    }
                });







            }
        });


        String title = getIntent().getStringExtra("TITLE");
        String url = getIntent().getStringExtra("URL");
         newsid = getIntent().getStringExtra("NEWSID");
         newsjson = getIntent().getStringExtra("NEWSJSON");
//        Log.e("Detail", "newsid = " + newsid + " newsjosn = " + newsjson);
        setTitle(title);
        news_webview.loadUrl(url);

        // 查询当前新闻的收藏状态
        queryLike();
    }

    private void queryLike() {
        // 如果当前用户未登录 则不需要查询
        if(BmobUser.getCurrentUser(BmobUser.class) == null){
            return;
        }

        String userid = BmobUser.getCurrentUser(BmobUser.class).getObjectId();

        final BmobQuery<Like> likeBmobQuery = new BmobQuery<>();
        likeBmobQuery.addWhereEqualTo("userid", userid);
        likeBmobQuery.addWhereEqualTo("newsid", newsid);
        likeBmobQuery.addWhereEqualTo("isLike", true);
        likeBmobQuery.findObjects(new FindListener<Like>() {
            @Override
            public void done(List<Like> list, BmobException e) {
                if(e == null){
                    if(list != null && list.size() > 0){
                        isLike = true;
                        iv_like.setImageResource(R.drawable.redstar);
                    }
                }
            }
        });
    }
}
