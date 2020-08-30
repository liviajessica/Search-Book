package id.ac.umn.projectuts_00000013091;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class GetBook extends RecyclerView.Adapter<GetBook.bookViewHolder>{

    private ArrayList<Books> bookList;
    private Context contexts;

    GetBook(ArrayList<Books> bookList, Context context){
        this.bookList = bookList;
        this.contexts=context;
    }

    @NotNull
    @Override
    public bookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.bookcard, viewGroup, false);
        return new bookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookViewHolder bookViewHolder, int i) {
        bookViewHolder.ASIN.setText(bookList.get(i).getAsin());
        bookViewHolder.TITLE.setText(bookList.get(i).getTitle());
        bookViewHolder.AUTHOR.setText(bookList.get(i).getAuthor());
    }

    @Override
    public int getItemCount() {
        return (bookList != null)? bookList.size() : 0;
    }
    class bookViewHolder extends RecyclerView.ViewHolder{
        private TextView ASIN, TITLE,AUTHOR;
        bookViewHolder(View itemView) {
            super(itemView);
            ASIN = itemView.findViewById(R.id.ASIN);
            TITLE = itemView.findViewById(R.id.TITLE);
            AUTHOR = itemView.findViewById(R.id.AUTHOR);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v){
                    Intent intent = new Intent(GetBook.this.contexts, BookDetail.class);
                    intent.putExtra("BookNo",ASIN.getText());
                    contexts.startActivity(intent);
                }
            });
        }
    }
}