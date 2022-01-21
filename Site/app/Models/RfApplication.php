<?php

namespace App\Models;

// use App\RfResource;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Rf_ap_id
 * @property int        $Rf_st_id
 * @property string     $Rf_ap_info
 * @property string     $Rf_ap_question
 * @property string     $Rf_ap_importer
 * @property Date       $Rf_ap_ann_date
 * @property Date       $Rf_ap_term_date
 * @property Date       $Rf_ap_reg_date
 * @property Date       $Rf_ap_date
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class RfApplication extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Rf_application';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Rf_ap_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Rf_ap_id', 'Rf_st_id', 'Rf_ap_info', 'Rf_ap_question', 'Rf_ap_importer', 'Rf_impT_id', 'Rf_ap_proposal',  'Rf_ap_ann_date', 'Rf_ap_ann_file', 'Rf_ap_term_date', 'Rf_ap_reg_date', 'Rf_ap_date', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'Rf_ap_id' => 'int', 'Rf_st_id' => 'int', 'Rf_ap_info' => 'string', 'Rf_ap_question' => 'string', 'Rf_ap_importer' => 'string', 'Rf_impT_id' => 'int', 'Rf_ap_proposal' => 'string', 'Rf_ap_ann_date' => 'date', 'Rf_ap_ann_file' => 'string', 'Rf_ap_term_date' => 'date', 'Rf_ap_reg_date' => 'date', 'Rf_ap_date' => 'date', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'Rf_ap_ann_date', 'Rf_ap_term_date', 'Rf_ap_reg_date', 'Rf_ap_date', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_status()
    {
        return $this->belongsTo(RfStatus::class, 'Rf_st_id');
    }
    public function eq_imp_type()
    {
        return $this->belongsTo(RfImporterType::class, 'Rf_impT_id');
    }
    public function eq_app()
    {
        return $this->hasMany(RfResource::class, 'Rf_ap_id');
    }
}
