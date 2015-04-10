package uk.gov.prototype.vitruvius;

public class Committer {
    private String repoUri;
    private String updatedBy;
    private String updatedOn;

    public Committer(String repoUri, String updatedBy, String updatedOn) {
        this.repoUri = repoUri;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
    }

    public String getRepoUri() {
        return repoUri;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Committer committer = (Committer) o;

        if (repoUri != null ? !repoUri.equals(committer.repoUri) : committer.repoUri != null) return false;
        if (updatedBy != null ? !updatedBy.equals(committer.updatedBy) : committer.updatedBy != null) return false;
        if (updatedOn != null ? !updatedOn.equals(committer.updatedOn) : committer.updatedOn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = repoUri != null ? repoUri.hashCode() : 0;
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        result = 31 * result + (updatedOn != null ? updatedOn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Committer{" +
                "repoUri='" + repoUri + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }
}

