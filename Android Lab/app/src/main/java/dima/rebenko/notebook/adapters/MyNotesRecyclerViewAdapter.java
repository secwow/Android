package dima.rebenko.notebook.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import dima.rebenko.notebook.Helpers.CustomTextView;
import dima.rebenko.notebook.R;
import dima.rebenko.notebook.model.Note;

public class MyNotesRecyclerViewAdapter extends RecyclerView.Adapter<MyNotesRecyclerViewAdapter.NoteItem>{

    private List<Note> mValues;
    protected final onRecyclerViewClickListener listener;

    View view;
    public MyNotesRecyclerViewAdapter(List<Note> items, onRecyclerViewClickListener listener) {
        mValues = items;
        this.listener = listener;
    }


    public void updateData(List<Note> viewModels) {
        this.mValues = viewModels;
        notifyDataSetChanged();
    }

    @Override
    public NoteItem onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_item, parent, false);

        return new NoteItem(view, listener);
    }

    @Override
    public void onBindViewHolder(final NoteItem holder, int position) {

        holder.mItem = mValues.get(position);
        if (!"".equals(holder.mItem.getPathToImage())) {
            Bitmap mainImage = BitmapFactory.decodeFile(holder.mItem.getPathToImage());
            if (mainImage != null) {
                holder.imageIcon.setImageBitmap(mainImage);
            }
        }
        int newColor;

        switch (holder.mItem.getImportance()){
            case NO_MATTER:
                holder.importantIcon.setImageResource(android.R.drawable.ic_dialog_alert);
                newColor = view.getResources().getColor(android.R.color.holo_blue_light);
                holder.importantIcon.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case IMPORTANT:
                Log.d("IMP","Import");
                holder.importantIcon.setImageResource(android.R.drawable.ic_dialog_alert);
                newColor = view.getResources().getColor(android.R.color.holo_green_dark);
                holder.importantIcon.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case MOST_IMPORTANT:
                holder.importantIcon.setImageResource(android.R.drawable.ic_dialog_alert);
                newColor = view.getResources().getColor(android.R.color.holo_orange_dark);
                holder.importantIcon.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                break;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        holder.date.setText(dateFormat.format(holder.mItem.getCreationalTime()));

    }

    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class NoteItem extends RecyclerView.ViewHolder {
        public final ImageView imageIcon;
        public final ImageView importantIcon;
        public final TextView date;
        public final View viewHolder;

        public Note mItem;

        public NoteItem(View view, onRecyclerViewClickListener listener) {
            super(view);
            viewHolder = view;
            date = CustomTextView.setupTextView((TextView) view.findViewById(R.id.date));
            imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
            importantIcon = (ImageView) view.findViewById(R.id.importantIcon);
            view.setOnClickListener(view1 -> listener.OnClick(view1, mItem));

            view.setOnLongClickListener(view1 -> {
                listener.onLongClick(view1, mItem);
                return false;
            });
        }
    }

    public interface onRecyclerViewClickListener {
        void OnClick(View view, Note note);
        void onLongClick(View view, Note note);
    }
}
