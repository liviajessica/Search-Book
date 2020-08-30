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

public class GetFavorite  extends RecyclerView.Adapter<GetFavorite.favoritesViewHolder> {
    private ArrayList<FavoriteBooks> datalist;
    private Context contexts;

    GetFavorite(ArrayList<FavoriteBooks> datalist, Context contexts){
        this.datalist=datalist;
        this.contexts=contexts;
    }

    @NotNull
    @Override
    public favoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bookcard, parent,false);
        return new favoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull favoritesViewHolder holder, int position){
        holder.ASIN.setText(datalist.get(position).ASIN);
        holder.TITLE.setText(datalist.get(position).TITLE);
        holder.AUTHOR.setText(datalist.get(position).AUTHOR);
    }

    @Override
    public int getItemCount(){
        return (datalist != null)? datalist.size() : 0;
    }
    class favoritesViewHolder extends RecyclerView.ViewHolder{
        private TextView ASIN, TITLE, AUTHOR;
        favoritesViewHolder(View itemView){
            super(itemView);
            ASIN = itemView.findViewById(R.id.ASIN);
            TITLE = itemView.findViewById(R.id.TITLE);
            AUTHOR=  itemView.findViewById(R.id.AUTHOR);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v){
                    Intent intent = new Intent(GetFavorite.this.contexts, BookDetail.class);
                    intent.removeExtra("BookNo");
                    intent.putExtra("BookNo",ASIN.getText());
                    contexts.startActivity(intent);
                }
            });

        }
    }
}
