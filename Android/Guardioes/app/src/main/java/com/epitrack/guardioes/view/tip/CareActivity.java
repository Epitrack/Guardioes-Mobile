package com.epitrack.guardioes.view.tip;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class CareActivity extends BaseAppCompatActivity {

    @Bind(R.id.care_content_01)
    TextView preventionContent;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.care);

        preventionContent.setText(Html.fromHtml("<p><b>Saúde do Viajante</b></p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Procure seu médico, preferencialmente, entre 4 e 8 semanas antes da viagem para informar seu roteiro. Peça orientações sobre cuidados para proteção contra doenças e lesões.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Você pode ter dificuldade ou não encontrar os medicamentos que utiliza habitualmente durante a viagem. Peça orientação ao seu médico sobre quais medicamentos e em que quantidade deve levar durante a viagem, incluindo a bagagem de mão.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Carregue com você os seus documentos de identificação, de preferência em inglês e português, com informações de contatos pessoais, tipo sanguíneo, se tem alergias, diabetes ou outras doenças que possam requerer particular atenção.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Toda gestante deve consultar seu médico antes da viagem, pois estará sujeita a vários riscos e a viagem pode afetar sua segurança e conforto.</p>\n" +
                "<p>&#8226;&nbsp;Antes de viajar consulte a empresa de transporte sobre as regras específicas para gestantes.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;O Brasil é um país de clima tropical. Recomenda-se ao viajante a ingestão constante de líquidos para evitar a desidratação.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Use roupas e calçados confortáveis. Eles lhe darão segurança e proteção contra torções, picadas de insetos e acidentes com animais peçonhentos.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Para se proteger do sol, cubra-se com roupas apropriadas, utilize chapéu ou boné e óculos escuros. Evite a exposição direta ao sol entre 10 horas da manhã e 4 horas da tarde.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Use protetor solar com fator de proteção adequado à cor de sua pele, de acordo com as orientações do fabricante. Mesmo em locais mais frios, sua pele ficará protegida dos raios solares.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Use repelentes quando houver necessidade.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;Lave as mãos com água e sabão várias vezes ao dia, principalmente antes de ingerir alimentos, após utilizar conduções públicas, visitar mercados ou locais com grande fluxo de pessoas.</p>\n" +
                "\n" +
                "<p>&#8226;&nbsp;No Brasil, é proibido por lei dirigir após o consumo de bebida alcoólica, mesmo em pequenas quantidades.</p>\n"));
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Basic Care Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
