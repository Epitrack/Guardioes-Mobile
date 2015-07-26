package com.epitrack.guardioes.view.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.Symptom;
import com.epitrack.guardioes.view.BaseAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SymptomActivity extends BaseAppCompatActivity {

    @Bind(R.id.symptom_activity_list_view)
    ListView listView;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.symptom_activity);

        ButterKnife.bind(this);

        final View footerView = LayoutInflater.from(this).inflate(R.layout.symptom_footer, null);

        footerView.findViewById(R.id.symptom_button_confirm).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                Toast.makeText(SymptomActivity.this, "Funcionalidade ainda não disponível", Toast.LENGTH_SHORT).show();
            }
        });

        listView.addFooterView(footerView);

        listView.setAdapter(new SymptomAdapter(this, Symptom.values()));
    }
}
