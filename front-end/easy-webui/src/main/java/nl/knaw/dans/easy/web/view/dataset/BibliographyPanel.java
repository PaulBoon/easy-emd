package nl.knaw.dans.easy.web.view.dataset;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.knaw.dans.easy.domain.model.Dataset;
import nl.knaw.dans.easy.web.template.AbstractEasyPanel;
import nl.knaw.dans.pf.language.emd.EasyMetadata;
import nl.knaw.dans.pf.language.emd.Term;
import nl.knaw.dans.pf.language.emd.types.EmdConstants;

public class BibliographyPanel extends AbstractEasyPanel<EasyMetadata> {
    private static final long serialVersionUID = -6945991727691872102L;

    private static final Logger logger = LoggerFactory.getLogger(BibliographyPanel.class);

    /** Wicket id. */
    public static final String NAME_LABEL = "authorName";
    public static final String DATE_LABEL = "publicationDate";
    public static final String TITLE_LABEL = "title";
    public static final String URL_LABEL = "url";

    public static final String SEPARATOR = "; ";

    public BibliographyPanel(String wicketId, IModel<EasyMetadata> model) {
        super(wicketId, model);
        init();
    }

    private void init() {
        EasyMetadata emd = (EasyMetadata) getDefaultModelObject();

        add(new Label(NAME_LABEL, getAuthors(emd)));
        String dateStr = getDate(emd);
        add(new Label(DATE_LABEL, dateStr).add(new SimpleAttributeModifier("datetime", dateStr)));
        String titleStr = getTitle(emd);
        add(new Label(TITLE_LABEL, titleStr).add(new SimpleAttributeModifier("title", titleStr)));

        // URL
        String doi = emd.getEmdIdentifier().getDansManagedDoi();
        String pid = emd.getEmdIdentifier().getPersistentIdentifier();
        if (!isBlank(doi)) {
            add(new Label(URL_LABEL, EmdConstants.DOI_RESOLVER + "/" + doi));
        } else {
            add(new Label(URL_LABEL, EmdConstants.BRI_RESOLVER + "?identifier=" + pid));
        }
    }

    private String getDate(EasyMetadata emd) {
        // the year of the dataset creation
        DateTime date = emd.getEmdDate().getDateCreated();
        if (date != null)
            return Integer.toString(date.getYear());
        else
            return "";
    }

    private String getAuthors(EasyMetadata emd) {
        // assume creators are Authors
        return emd.toString(SEPARATOR, Term.Name.CREATOR);
    }

    private String getTitle(EasyMetadata emd) {
        return emd.getPreferredTitle();
    }
}
