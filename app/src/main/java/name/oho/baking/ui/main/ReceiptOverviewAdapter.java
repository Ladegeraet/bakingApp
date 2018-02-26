package name.oho.baking.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.oho.baking.R;
import name.oho.baking.model.Receipt;
import name.oho.baking.network.PicassoVideoRequestHandler;


/**
 * Created by tobi on 17.02.18.
 */

public class ReceiptOverviewAdapter extends ReceiptOverviewRecyclerView.Adapter<ReceiptOverviewAdapter.ReceiptOverviewViewHolder> {

    private int mNumberItems;
    private List<Receipt> mReceiptList;

    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int listItemIndex);
    }

    public ReceiptOverviewAdapter(List<Receipt> receiptList, ListItemClickListener onClickListener) {
        if (receiptList == null) {
            receiptList = new ArrayList<>();
        }

        mNumberItems = receiptList.size();
        mReceiptList = receiptList;
        mOnClickListener = onClickListener;
    }

    @Override
    public ReceiptOverviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.receipt_overview_list_item, parent, false);
        return new ReceiptOverviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiptOverviewViewHolder holder, int position) {
        holder.bind(mReceiptList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    public class ReceiptOverviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_receipt_poster)
        ImageView poster;

        @BindView(R.id.tv_receipt_name)
        TextView name;

        public ReceiptOverviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bind(Receipt receipt) {

            String imageUrl = receipt.findLastThumbnailURL();

            if (imageUrl.isEmpty()) {
                Picasso picasso = new Picasso.Builder(itemView.getContext()).addRequestHandler(new PicassoVideoRequestHandler()).build();
                picasso.load(receipt.findLastVideoURL())
                        .placeholder(R.drawable.pexels)
                        .error(R.drawable.ic_error)
                        .into(poster);
            } else {
                Picasso.with(itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.pexels)
                        .error(R.drawable.ic_error)
                        .into(poster);
            }

            name.setText(receipt.getName());
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
