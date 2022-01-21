<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Pl_Prog_id
 * @property Date       $Pl_Prog_date1
 * @property Date       $Pl_Prog_date2
 * @property int        $Pl_ProgT_id
 * @property int        $Pl_ProgS_id
 * @property int        $C_St_id
 * @property int        $sync_ID
 * @property int        $A_ns_C_id
 * @property Date       $Pl_Prog_date1_old
 * @property Date       $Pl_Prog_date2_old
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class PlProgram extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Pl_Program';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Pl_Prog_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Pl_Prog_id', 'Pl_Prog_date1', 'Pl_Prog_date2', 'Pl_ProgT_id', 'Pl_ProgS_id', 'Pl_Prog_cal', 'C_St_id', 'sync_ID', 'A_ns_C_id', 'Pl_Prog_date1_old', 'Pl_Prog_date2_old', 'created_at', 'updated_at', 'deleted_at'
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
        'Pl_Prog_id' => 'int', 'Pl_Prog_date1' => 'date', 'Pl_Prog_date2' => 'date', 'Pl_ProgT_id' => 'int', 'Pl_ProgS_id' => 'int', 'C_St_id' => 'int', 'sync_ID' => 'int', 'A_ns_C_id' => 'int', 'Pl_Prog_date1_old' => 'date', 'Pl_Prog_date2_old' => 'date', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'Pl_Prog_date1', 'Pl_Prog_date2', 'Pl_Prog_date1_old', 'Pl_Prog_date2_old', 'created_at', 'updated_at', 'deleted_at'
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
    public function eq_program()
    {
        return $this->HasMany(PlProgramPoints::class, 'Pl_Prog_id');
    }

    public function eq_type()
    {
        return $this->belongsTo(PlProgramType::class, 'Pl_ProgT_id');
    }
}
