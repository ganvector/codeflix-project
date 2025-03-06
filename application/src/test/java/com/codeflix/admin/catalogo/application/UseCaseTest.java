package com.codeflix.admin.catalogo.application;

import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public abstract class UseCaseTest implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        Mockito.reset(getMocks());
    }

    protected abstract List<Object> getMocks();

    protected Set<String> asString(final Collection<? extends Identifier> identifiers) {
        return new HashSet<>(
                identifiers.stream()
                        .map(Identifier::getValue)
                        .toList()
        );
    }
}
